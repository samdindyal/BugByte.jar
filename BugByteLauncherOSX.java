public class BugByteLauncherOSX
{
	public static void main(String[] args)
	{

			javax.swing.SwingUtilities.invokeLater(new Runnable() {
      		@Override
      		public void run()
      		{
              if (System.getProperty("os.name").startsWith("Mac OS X"))
              {
                    com.apple.eawt.Application macApp = com.apple.eawt.Application.getApplication();
                    macApp.setDockIconImage (new javax.swing.ImageIcon (getClass().getResource ("res/logo.png")).getImage ());
              }
     			    BugByteUI bugByteUI = new BugByteUI();   	
      		}
    	});
	}
}