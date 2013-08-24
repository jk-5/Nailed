class Config(dict):
    def __init__(self, filename='nailed.conf'):
        with open(filename, 'r') as config:
            for line in config.readlines():

                line = line.strip()

                if line == '': continue
                if line[0] == '#': continue
                
                try:
                    key, value = tuple(line.split('='))
                    self[key] = value
                except Exception as e:
                    print e
                    pass

    def __getattr__(self, name):
        if not name in self:
            raise AttributeError(name)
            return

        return self[name]

    def __setattr__(self, name, value):
        self[name] = value
