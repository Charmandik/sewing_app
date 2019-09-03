package fishrungames.tes;

public class JniWrapper
{
    static {
        System.loadLibrary("engine");
        System.loadLibrary("template");
    }

   
    public static native void Init(int width, int height);
    
}