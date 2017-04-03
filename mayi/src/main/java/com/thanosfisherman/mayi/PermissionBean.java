package com.thanosfisherman.mayi;

public class PermissionBean
{
    public final String name;
    private boolean isGranted;
    private boolean shouldShowRequestPermissionRationale;
    private boolean isPermanentlyDenied;

    PermissionBean(String name)
    {
        this(name, false, false);
    }

    private PermissionBean(String name, boolean isGranted, boolean shouldShowRequestPermissionRationale)
    {
        this.name = name;
        this.isGranted = isGranted;
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

        return isGranted == that.isGranted && shouldShowRequestPermissionRationale == that.shouldShowRequestPermissionRationale && name.equals(that.name);
    }

    @Override
    public int hashCode()
    {
        int result = name.hashCode();
        result = 31 * result + (isGranted ? 1 : 0);
        result = 31 * result + (shouldShowRequestPermissionRationale ? 1 : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Permission{" + "name='" + name + '\'' + ", isGranted=" + isGranted + ", shouldShowRequestPermissionRationale=" +
               shouldShowRequestPermissionRationale + ", isPermanentlyDenied=" + isPermanentlyDenied + '}';
    }

    public String getName()
    {
        return name;
    }

    public boolean isGranted()
    {
        return isGranted;
    }

    public boolean isShouldShowRequestPermissionRationale()
    {
        return shouldShowRequestPermissionRationale;
    }

    void setGranted(boolean granted)
    {
        this.isGranted = granted;
    }

    void setShouldShowRequestPermissionRationale(boolean shouldShowRequestPermissionRationale)
    {
        this.shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale;
    }

    public boolean isPermanentlyDenied()
    {
        return isPermanentlyDenied;
    }

    public void setPermanentlyDenied(boolean permanentlyDenied)
    {
        isPermanentlyDenied = permanentlyDenied;
    }
}
