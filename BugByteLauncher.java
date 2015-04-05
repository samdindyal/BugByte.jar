public class BugByteLauncher
{
	public static void main(String[] args)
	{

			javax.swing.SwingUtilities.invokeLater(new Runnable() {
      		@Override
      		public void run()
      		{
     			BugByteUI bugByteUI = new BugByteUI();   	
      		}
    	});
	}
}