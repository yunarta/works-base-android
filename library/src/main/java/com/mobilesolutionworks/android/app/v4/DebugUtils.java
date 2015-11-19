package com.mobilesolutionworks.android.app.v4;

/**
 * Created by yunarta on 19/11/15.
 */
public class DebugUtils
{
    public static void buildShortClassTag(Object cls, StringBuilder out)
    {
        if (cls == null)
        {
            out.append("null");
        }
        else
        {
            String simpleName = cls.getClass().getSimpleName();
            if (simpleName == null || simpleName.length() <= 0)
            {
                simpleName = cls.getClass().getName();
                int end = simpleName.lastIndexOf('.');
                if (end > 0)
                {
                    simpleName = simpleName.substring(end + 1);
                }
            }
            out.append(simpleName);
            out.append('[');
            out.append(Integer.toString(System.identityHashCode(cls), Character.MAX_RADIX));
        }
    }
}
