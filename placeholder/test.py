from os.path import expanduser
import shutil
home = expanduser("~")
shutil.rmtree(home+"/favorites")
