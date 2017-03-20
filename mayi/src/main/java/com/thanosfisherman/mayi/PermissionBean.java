package com.thanosfisherman.mayi;

public class PermissionBean
{
    public final String name;
    private boolean granted;
    private boolean shouldShowRequestPermissionRationale;

    PermissionBean(String name)
    {
        this(name, false, false);
    }

    private PermissionBean(String name, boolean granted, boolean shouldShowRequestPermissionRationale)
    {
        this.name = name;
        this.granted = granted;
        this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final PermissionBean that = (PermissionBean) o;

        return granted == that.granted && shouldShowRequestPermissionRationale == that.shouldShowRequestPermissionRationale && name.equals(that.name);
    }

    @Override
    public int hashCode()
    {
        int result = name.hashCode();
        result = 31 * result + (granted ? 1 : 0);
        result = 31 * result + (shouldShowRequestPermissionRationale ? 1 : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Permission{" +
               "name='" + name + '\'' +
               ", granted=" + granted +
               ", shouldShowRequestPermissionRationale=" + shouldShowRequestPermissionRationale +
               '}';
    }

    public String getName()
    {
        return name;
    }

    public boolean isGranted()
    {
        return granted;
    }

    public boolean isShouldShowRequestPermissionRationale()
    {
        return shouldShowRequestPermissionRationale;
    }

    void setGranted(boolean granted)
    {
        this.granted = granted;
    }

    void setShouldShowRequestPermissionRationale(boolean shouldShowRequestPermissionRationale)
    {
        this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale;
    }
}
