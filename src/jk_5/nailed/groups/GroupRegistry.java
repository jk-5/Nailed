package jk_5.nailed.groups;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * TODO: Edit description
 *
 * @author jk-5
 */
public class GroupRegistry {

    final BiMap<String, Group> groups = HashBiMap.create();
    private Group defaultGroup;

    public void registerGroup(String name, Group group){
        this.groups.put(name, group);
    }

    public Group getGroup(String name){
        return this.groups.get(name);
    }

    public Group getDefaultGroup(){
        return this.defaultGroup;
    }

    public void setDefaultGroup(String name){
        Group g = this.getGroup(name);
        if(g != null) this.defaultGroup = g;
    }

    public Iterable<String> getGroups(){
        return this.groups.keySet();
    }
}
