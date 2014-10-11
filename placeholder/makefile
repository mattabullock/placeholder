CC = g++

# LDFLAGS = -I ./boost_1_56_0/
# LLIBFLAGS = -L ./boost_1_56_0/libs/
LDFLAGS = -I /usr/local/Cellar/boost/1.56.0/include/
LLIBFLAGS = -L /usr/local/Cellar/boost/1.56.0/lib/
LINKFLAGS = -lboost_system-mt -lboost_thread-mt

FLAGS = $(LDFLAGS) $(LLIBFLAGS) $(LINKFLAGS)

all: 
	$(CC) $(FLAGS) -o placeholder placeholder.cpp

clean:
	rm -rf *o all
