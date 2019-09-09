# Navigation

This is the toy app for lesson 3 of the [Android App Development in Kotlin course on Udacity](https://www.udacity.com/course/developing-android-apps-with-kotlin--ud9012).

## Android Trivia 

The Android Trivia application is an application that asks the user trivia questions about Android development.  It makes use of the Navigation component within Jetpack to move the user between different screens.  Each screen is implemented as a Fragment.
The app navigates using buttons, the Action Bar, and the Navigation Drawer.
Since students haven't yet learned about saving data or the Android lifecycle, it tries to eliminate bugs caused by configuration changes. 

**1. Add navigation to project level gradle**
```gradle
buildscript {
    ext {
        ...
        version_navigation = '1.0.0'
        ...
    }
```

**2. Add dependencies to app level gradle**
```gradle
dependencies {
    ...
    implementation "android.arch.navigation:navigation-fragment-ktx:$version_navigation"     
    implementation "android.arch.navigation:navigation-ui-ktx:$version_navigation"
}
```
##Adding the Navigation Graph to the Project

In the Project window, right-click on the res directory and select New > Android resource file. The New Resource dialog appears.
Select Navigation as the resource type, and give it the file name of navigation. Make sure it has no qualifiers. Select the navigation.xml file in the new navigation directory under res, and make sure the design tab is selected.

**1. Replace the Title Fragment with the Navigation Host Fragment in the Activity Layout **

Go to the activity_main layout. Change the class name of the existing Title fragment to androidx.navigation.fragment.NavHostFragment. Change the ID to myNavHostFragment. It needs to know which navigation graph resource to use, so add the app:navGraph attribute and have it point to the navigation graph resource - @navigation/navigation. Finally, set defaultNavHost = true, which means that this navigation host will intercept the system back key.

```xml
<!-- The NavHostFragment within the activity_main layout -->
<fragment
   android:id="@+id/myNavHostFragment"
   android:name="androidx.navigation.fragment.NavHostFragment"
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   app:navGraph="@navigation/navigation"
   app:defaultNavHost="true"
   />
```

**2. Adding the Title and Game Fragments to the Navigation Graph**

Within the navigation editor, click the add button. A list of fragments and activities will drop down. Add fragment_title first, as it is the start destination. (you’ll see that it will automatically be set as the Start Destination for the graph.) Next, add the fragment_game.

```xml
<!-- The complete game fragment within the navigation XML, complete with tools:layout. -->
<fragment
   android:id="@+id/gameFragment"
   android:name="com.example.android.navigation.GameFragment"
   android:label="GameFragment"
   tools:layout="@layout/fragment_game" />
```

**3. Connecting the Title and Game Fragments with an Action**
Begin by hovering over the titleFragment. You’ll see a circular connection point on the right side of the fragment view. Click on the connection point and drag it to gameFragment to add an Action that connects the two fragments.

**4. Navigating when the Play Button is Hit**

Return to onCreateView in the TitleFragment Kotlin code. The binding class has been exposed, so you just call binding.playButton.setOnClickListener with a new anonymous function, otherwise known as a lambda. Inside our lambda, use view.findNavcontroller to get the navigation controller for our Navigation Host Fragment. Then, use the navController to navigate using the titleFragment to gameFragment action, by calling navigate(R.id.action_titleFragment_to_gameFragment)

```kotlin
//The complete onClickListener with Navigation
binding.playButton.setOnClickListener { view: View ->
//      Navigation.findNavController(view).navigate(R.id.action_titleFragment_to_gameFragment)
        view.findNavController().navigate(R.id.action_titleFragment_to_gameFragment)
}
```

or, one more thing you might want to do instead. Navigation can create the onClick listener for us. We can replace the anonymous function with the Navigation.createNavigateOnClickListener call.

```kotlin
//The complete onClickListener with Navigation using createNavigateOnClickListener
binding.playButton.setOnClickListener(
        Navigation.createNavigateOnClickListener(R.id.action_titleFragment_to_gameFragment))

```

Done! Now clicking the Play button from the fragment_title will send us to the fragment_game, and pressing back from the fragment_game  will take us back to the fragment_title.




------------------------------------------------------------------------

## Back Stack Manipulation

**1. For the action connecting the gameFragment to the gameOverFragment, set the pop behavior to popTo gameFragment inclusive**

Go to the navigation editor and select the action for navigating from the GameFragment to the GameOverFragment. Select PopTo GameFragment in the attributes pane with the inclusive flag. 
This will tell the Navigation component to pop fragments off of the fragment back stack until it finds the GameFragment, and then pop off the gameFragment transaction.

If we hadn't set it to inclusive, it would have allowed the game fragment transaction to execute.

Do the same for the GameFragment to the GameWonFragment.
Now regardless of if we win or less, pressing the phone back button will take us to the title fragment


## Up Navigation

**1. Link the NavController to the ActionBar with NavigationUI.setupActionBarWithNavController**

Move to MainActivity. We need to find the NavController. Since we’re in the Activity now, we’ll use the alternate method of finding the controller from the ID of our NavHostFragment using the KTX extension function.
```kotlin
val navController = this.findNavController(R.id.myNavHostFragment)
```
Link the NavController to our ActionBar.
```kotlin
NavigationUI.setupActionBarWithNavController(this, navController)
```

**2. Override the onSupportNavigateUp method from the activity and call navigateUp in nav controller**
Finally, we need to have the Activity handle the navigateUp action from our Activity. To do this we override onSupportNavigateUp, find the nav controller, and then we call navigateUp().

```kotlin
override fun onSupportNavigateUp(): Boolean {
   val navController = this.findNavController(R.id.myNavHostFragment)
   return navController.navigateUp()
}
```

-----------------------------------------------------------------------

## Adding a Menu

**1. Add AboutFragment to the navigation graph**

Click the "add" button. A list of fragments and activities will drop down. Add fragment_about. Name it with the title_about_trivia string. Set its id to aboutFragment. The menu will need this id to navigate to the correct fragment.

**2. Create new menu resource.**

Right click on the res folder within the Android project and select New Resource File. We’ll call this one overflow_menu, with resource type of Menu. Click on the overflow_menu within the menu directory, to view our new (empty) menu.

**3. Create “About” menu item with ID of aboutFragment destination**

Make sure the design tab is selected. Drag a menu item from the palette into the component tree below. Move to the attributes pane. Set the new item's id to aboutFragment, its destination. That's the id you used when adding the About fragment to the navigation graph. For title, we can use @string/about. The rest of the attributes should be left as their defaults.

**4. Call setHasOptionsMenu() in onCreateView of TitleFragment**

Next we need to tell Android that our TitleFragment has a menu. In onCreateView call setHasOptionsMenu(true).

```kotlin
override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                         savedInstanceState: Bundle?): View? {
   ...
   setHasOptionsMenu(true)
   return binding.root
}
```

**5. Override onCreateOptionsMenu and inflate menu resource**

Next we need to override onCreateOptionsMenu and inflate our new menu resource using the provided menu inflater and menu structure.

```kotlin
override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
   super.onCreateOptionsMenu(menu, inflater)
   inflater?.inflate(R.menu.overflow_menu, menu)
}
```

**6. Override onOptionsItemSelected and call NavigationUI.onNavDestinationSelected**

Finally, we need to override onOptionsItemSelected to connect it to our NavigationUI.
```kotlin
override fun onOptionsItemSelected(item: MenuItem?): Boolean {
   return NavigationUI.onNavDestinationSelected(item!!,
           view!!.findNavController())
           || super.onOptionsItemSelected(item)
}
```

----------------------------------------------------------------------


## Add Safe Arguments (to pass parameters / data between Fragments)

**Fragment A ---> Bundle ---> Fragment B**
```kotlin
// Fragment A
val argBundle = Bundle()
argBundle.putString(NAME_KEY_STRING, "content")
argBundle.putInt(SERIAL_KEY_INT, 42)

...

// Fragment B
val fragment = FragmentB()
fragment.arguments = argBundle

```
This works, but is not ideal. There are ways it can generate bugs. 
The types must match:
```kotlin
val name = arguments.getInt(NAME_KEY_STRING)     // <--- getInt on a String value
val serial = arguments.getString(SERIAL_KEY_INT) // <--- getString on a Int value
```
Getting an Integer from a string returns the default **value 0**.
Getting a String from a Integer returns the default **value null**.
What you send in Fragment A isn't necessarely what Fragment B needs or asks for.
