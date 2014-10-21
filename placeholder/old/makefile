CC = g++

OLDFLAGS = -I /usr/local/Cellar/boost/1.56.0/include/
OLLIBFLAGS = -L /usr/local/Cellar/boost/1.56.0/lib/

WLDFLAGS = -I C:\local\boost_1_56_0\
# WLLIBFLAGS = -L C:\local\boost_1_56_0\libs

LIBSSH = -I ./libssh-0.6.3/include/ -lssh

LINKFLAGS = -lboost_system-mt -lboost_thread-mt

WINFLAGS = $(WLDFLAGS) $(WLLIBFLAGS)

OSXFLAGS = $(OLDFLAGS) $(OLLIBFLAGS) $(LINKFLAGS) $(LIBSSH)

osx: 
	$(CC) $(OSXFLAGS) -o placeholder placeholder.cpp

windows:
	$(CC) $(WINFLAGS) -o placeholder.exe placeholder.cpp

clean:
	rm -rf *o all
