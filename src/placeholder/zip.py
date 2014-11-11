import os
import zipfile

def zipdir(path, zippath):
    os.chdir(path)
    path = "."
    zip = zipfile.ZipFile(zippath, 'w', allowZip64=True)
    for root, dirs, files in os.walk(path):
        for file in files:
            zip.write(os.path.join(root, file))
    zip.close()

if __name__ == '__main__':
    zipf = zipfile.ZipFile('/Users/matt/Desktop/Python.zip', 'w', allowZip64=True)
    os.chdir('/Users/matt/Documents')
    zipdir('.', zipf)
    zipf.close()