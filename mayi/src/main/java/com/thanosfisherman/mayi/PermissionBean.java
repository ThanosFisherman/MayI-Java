package com.thanosfisherman.mayi;

import android.support.annotation.NonNull;

public class PermissionBean
{
    @NonNull public final String name;
    private boolean isGranted;
    private boolean isPermanentlyDenied;

    PermissionBean(String name)
    {
        this(name, false);
    }

    private PermissionBean(@NonNull String name, boolean isGranted)
    {
        this.name = name;
        this.isGranted = isGranted;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final PermissionBean that = (PermissionBean) o;

        return isGranted == that.isGranted && name.equals(that.name);
    }

    @Override
    public int hashCode()
    {
        int result = name.hashCode();
        result = 31 * result + (isGranted ? 1 : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Permission{" + "name='" + getSimpleName() + "'" + ", isGranted=" + isGranted() + ", isPermanentlyDenied=" + isPermanentlyDenied() + "}";
    }

    @NonNull
    public String getName()
    {
        return name;
    }

    public String getSimpleName()
    {
        try
        {
            return name.split("\\.")[2];
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            return name;
        }
    }

    public boolean isGranted()
    {
        return isGranted;
    }

    void setGranted(boolean granted)
    {
        this.isGranted = granted;
    }

    public boolean isPermanentlyDenied()
    {
        return isPermanentlyDenied;
    }

    void setPermanentlyDenied(boolean permanentlyDenied)
    {
        isPermanentlyDenied = permanentlyDenied;
    }
}
