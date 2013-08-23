#!/usr/bin/env python

import os, os.path, sys, zipfile, urllib2, shlex, shutil, glob, fnmatch, subprocess
from pprint import pformat

sys.stdout = os.fdopen(sys.stdout.fileno(), 'w', 0)
nailed_dir = os.path.dirname(os.path.abspath(__file__))

def main():
    print '=== Setup Start ==='

    mcp_dir = os.path.join(nailed_dir, 'mcp')

    if not os.path.exists(mcp_dir):
        mcp_zip = os.path.join(nailed_dir, 'mcp.zip')
        if not os.path.exists(mcp_zip):
            print('Downloading mcp...')
            u = urllib2.urlopen('http://mcp.ocean-labs.de/files/archive/mcp805.zip')
            localFile = open(mcp_zip, 'w')
            localFile.write(u.read())
            localFile.close()
        print('Unzipping mcp')
        os.mkdir(mcp_dir)
        unzip(mcp_zip, mcp_dir)

    sys.path.append(mcp_dir)

    from runtime.decompile import decompile

    os.makedirs(os.path.join(mcp_dir,'jars','versions','1.6.2'))
    shutil.copy(os.path.join(nailed_dir,'jsons','1.6.2-dev.json'),os.path.join(mcp_dir,'jars','versions','1.6.2','1.6.2.json'))

    print('Downloading minecraft server...')
    u = urllib2.urlopen('http://s3.amazonaws.com/Minecraft.Download/versions/1.6.2/minecraft_server.1.6.2.jar')
    localFile = open(os.path.join(mcp_dir,'jars','minecraft_server.jar'), 'w')
    localFile.write(u.read())
    localFile.close()

    os.chdir(mcp_dir)
    #         conf  jad    csv    no-recompile no-comments no-reformat no-rename no-patch only-patch keep lvt keep generics client server rg     work dir                      json  nocopy
    decompile(None, False, False, True,        False,      False,      False,    False,   False,     False,   False,        False, True,  False, os.path.join(mcp_dir,'jars'), None, True)
    os.chdir(nailed_dir)

    src_dir = os.path.join(mcp_dir, 'src')

    base_dir = os.path.join(mcp_dir, 'src_base')
    work_dir = os.path.join(mcp_dir, 'src_work')

    if os.path.isdir(base_dir):
        shutil.rmtree(base_dir)
    if os.path.isdir(work_dir):
        shutil.rmtree(work_dir)

    print 'Setting up source directories'
    shutil.copytree(src_dir, base_dir)
    shutil.copytree(src_dir, work_dir)

    print 'Applying nailed patches'
    apply_patches(mcp_dir, os.path.join(nailed_dir,'patches'), work_dir)

    print '=== Setup Finished ==='

def cmdsplit(args):
    if os.sep == '\\':
        args = args.replace('\\', '\\\\')
    return shlex.split(args)

def copy_files(src_dir, dest_dir):
    for file in glob.glob(os.path.join(src_dir, '*')):
        if not os.path.isfile(file) or file.lower().endswith('.java'):
            continue
        print('    ' + file)
        shutil.copy(file, os.path.join(dest_dir, os.path.basename(file)))

def apply_patches(mcp_dir, patch_dir, target_dir, find=None, rep=None):
    # Attempts to apply a directory full of patch files onto a target directory.
    sys.path.append(mcp_dir)
    
    temp = os.path.abspath('temp.patch')
    cmd = cmdsplit('patch -p2 -i "%s" ' % temp)
    
    if os.name == 'nt':
        applydiff = os.path.abspath(os.path.join(mcp_dir, 'runtime', 'bin', 'applydiff.exe'))
        cmd = cmdsplit('"%s" -uf -p2 -i "%s"' % (applydiff, temp))
    
    for path, _, filelist in os.walk(patch_dir, followlinks=True):
        for cur_file in fnmatch.filter(filelist, '*.patch'):
            patch_file = os.path.normpath(os.path.join(patch_dir, path[len(patch_dir)+1:], cur_file))
            target_file = os.path.join(target_dir, fix_patch(patch_file, temp, find, rep))
            process = subprocess.Popen(cmd, cwd=target_dir, bufsize=-1)
            process.communicate()

    if os.path.isfile(temp):
        os.remove(temp)

def fix_patch(in_file, out_file, find=None, rep=None):
    #Fixes the following issues in the patch file if they exist:
    #  Normalizes the path seperators for the current OS
    #  Normalizes the line endings
    # Returns the path that the file wants to apply to
    
    in_file = os.path.normpath(in_file)
    if out_file is None:
        tmp_file = in_file + '.tmp'
    else:
        out_file = os.path.normpath(out_file)
        tmp_file = out_file
        dir_name = os.path.dirname(out_file)
        if dir_name:
            if not os.path.exists(dir_name):
                os.makedirs(dir_name)
                
    file = 'not found'
    with open(in_file, 'rb') as inpatch:
        with open(tmp_file, 'wb') as outpatch:
            for line in inpatch:
                line = line.rstrip('\r\n')
                if line[:3] in ['+++', '---', 'Onl', 'dif']:
                    if not find == None and not rep == None:
                        line = line.replace('\\', '/').replace(find, rep).replace('/', os.sep)
                    else:
                        line = line.replace('\\', '/').replace('/', os.sep)
                    outpatch.write(line + os.linesep)
                else:
                    outpatch.write(line + os.linesep)
                if line[:3] == '---':
                    file = line[line.find(os.sep, line.find(os.sep)+1)+1:]
                    
    if out_file is None:
        shutil.move(tmp_file, in_file)
    return file   

def run_command(command, cwd='.', verbose=True):
    print('Running command: ')
    print(pformat(command))

    process = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, bufsize=1, cwd=cwd)
    while process.poll() is None:
        line = process.stdout.readline()
        if line:
            line = line.rstrip()
            print(line)
    if process.returncode:
        print "failed: {0}".format(process.returncode)
        return False
    return True

def unzip(zipFilePath, destDir):
    zfile = zipfile.ZipFile(zipFilePath)
    if not os.path.exists(destDir):
        os.mkdir(destDir)
    for name in zfile.namelist():
        (dirName, fileName) = os.path.split(name)
        if not os.path.exists(os.path.join(destDir, dirName)) and dirName != '':
            os.makedirs(os.path.join(destDir, dirName))
        if fileName == '':
            newDir = os.path.join(destDir, dirName)
            if not os.path.exists(newDir):
                os.mkdir(newDir)
        else:
            fd = open(os.path.join(destDir, name), 'wb')
            fd.write(zfile.read(name))
            fd.close()
    zfile.close()

if __name__ == '__main__':
    main()