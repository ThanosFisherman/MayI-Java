MayI
-----

<img src="Logotype primary.png" width="40%" height="40%" />

MayI is yet another library that simplifies the process of requesting permissions at runtime for devices that run Android Marshmallow and above.

As of Androids Marshmallow and above a new functionality has been added that lets users grant or deny permissions while an app is running instead of granting them all
together when installing it. This approach gives the user more control over applications but requires developers to add lots of code to support it.

This library aims to reduce boilerplate code needed to request permissions at runtime by featuring a simple chainable API designed the way I want it.

Screenshot
-----------

![Demo screenshot](mayi_screenshot.gif "gif demo")

Usage
-----

### Single Permission
To request a **single permission** using this library, you just need to call `Mayi` with a valid `Activity` and use `withPermission` method:

```java
public MainActivity extends AppCompatActivity 
{
	@Override 
	public void onCreate() 
	{
	    super.onCreate();
	    Mayi.withActivity(MainActivity.this)
            .withPermission(Manifest.permission.READ_CONTACTS)
            .onResult(permission -> permissionResultSingle(permission))
            .onRationale((permission, token) -> permissionRationaleSingle(permission,token))
            .check()
	}
}
```

`permissionResultSingle` and `permissionRationaleSingle` could be custom-defined methods of your own that would deal accordingly in each situation. For Example:

```java
 private void permissionResultSingle(PermissionBean permission)
 {
     Toast.makeText(MainActivity.this, "PERMISSION RESULT " + permission.toString(), Toast.LENGTH_LONG).show();
 }
 
 private void permissionRationaleSingle(PermissionBean bean, PermissionToken token)
 {
     Toast.makeText(MainActivity.this, "Should show rationale for " + bean.getSimpleName() + " permission", Toast.LENGTH_LONG).show();
     token.skipPermissionRequest();   
 }
```

### Multiple Permissions
Similarly to request **multiple permissions** at once, you just need to call `Mayi` with a valid `Activity` but this time use `withPermissions` method to specify more than one permissions. Furthermore
the lambda expressions from the example above could be replaced with method references like so:

```java
public MainActivity extends AppCompatActivity 
{
	@Override 
	public void onCreate() 
	{
	    super.onCreate();
	    Mayi.withActivity(this)
            .withPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION)
            .onRationale(this::permissionRationaleMulti)
            .onResult(this::permissionResultMulti)
            .check()
	}
}
```

Again possible custom-defined methods for the above example could be something like:

```java
private void permissionResultMulti(PermissionBean[] permissions)
{
    Toast.makeText(MainActivity.this, "MULTI PERMISSION RESULT " + Arrays.deepToString(permissions), Toast.LENGTH_LONG).show();
}

private void permissionRationaleMulti(PermissionBean[] permissions, PermissionToken token)
{
    Toast.makeText(MainActivity.this, "Rationales for Multiple Permissions " + Arrays.deepToString(permissions), Toast.LENGTH_LONG).show();
    token.continuePermissionRequest();
}
```

### Error handling
If you think there is going to be an error in your Mayi integration, just call a `onErrorListener`:

```java
    Mayi.withActivity(this)
    .withPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION)
    .onRationale(this::permissionRationaleMulti)
    .onResult(this::permissionResultMulti)
    .onErrorListener(this::inCaseOfError)
    .check());
    
private void inCaseOfError(Exception e)
{
    Toast.makeText(MainActivity.this, "ERROR " + e.toString(), Toast.LENGTH_SHORT).show();
}
```

The library will then notify you when something unexpected happens.

Add it to your project
----------------------
[![Download](https://api.bintray.com/packages/thanosfisherman/maven/mayi/images/download.svg)](https://bintray.com/thanosfisherman/maven/mayi/_latestVersion)

Add the following to your **app module** `build.gradle` file

```groovy
dependencies {
   compile 'com.thanosfisherman.mayi:mayi:1.4'
}
```

Library Flow
------------
* The first time this library runs, system permission promt will appear asking for the user to either deny or allow the permission.
Next `onResult()` method will be called that includes the result of the user's choice.
* If user denied the permission the first time (but didn't check "don't ask again" option) then `onRationale` will be the first method to be called next
time this library runs. Inside `onRationale` method you now have 3 options. 
    * Call `token.continuePermissionRequest()` method which shows again system dialog prompt and then calls `onResult()` that includes the user's choice.
    * Call `token.skipPermissionRequest()` method which will skip showing system dialog prompt and immediately call `onResult()` that includes the user's choice.
    * Call none of the 2 above thus terminating the flow after `onRationale` finishes its execution.
* If user denied the permission by checking _"don't ask again"_ then `onResult()` will be called that includes the result of the user's choice. You may check whether the permission has been permanently denied via the `PermissionBean#isPermanentlyDenied()` method which is included in the `onResult()`.

below is a flow chart that visualizes the library's flow described above.

<img src="mayi_flow.png" alt="mayi flow" title="flow chart" style="width: auto; height: auto; max-width: 680px; max-height: 788px"/>

Contributing?
--------------------------

Feel free to add/correct/fix something to this library, I will be glad to improve it with your help.

License
-------
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square)](https://www.apache.org/licenses/LICENSE-2.0.html)

    Copyright 2017 Thanos Psaridis

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

