/**
  Title:        The "BugByteLauncher" class
  Date Written: March 2015 - April 2015
  Author:       Samuel Dindyal
  Description:  A launcher for the Java application "BugByte". It runs accordingly to the OS it is being run on.
*/

public class BugByteLauncher
{
	public static void main(String[] args)
	{

			javax.swing.SwingUtilities.invokeLater(new Runnable() {
      		@Override
      		public void run()
      		{
              //If "BugByte" is being run on Mac OS X, set the dock icon.
              if (System.getProperty("os.name").startsWith("Mac OS X"))
              {
                    com.apple.eawt.Application macApp = com.apple.eawt.Application.getApplication();
                    macApp.setDockIconImage (new javax.swing.ImageIcon (getClass().getResource ("res/logo.png")).getImage ());
              }

              //Create a new "BugByteUI" object and instantiate it
     			    BugByteUI bugByteUI = new BugByteUI();   	

              //Make the UI visible
              bugByteUI.show();
      		}
    	});
	}
}