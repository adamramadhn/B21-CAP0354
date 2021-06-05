Smart Fruit Analyzer - B21-CAP0354 (Android)
=

Team
-
- Adam Ramadhan - A1511725
- Muharroman Attoriq Zayzda - A0080846
- Juandito Batara Kuncoro - M0080845

Capstone Project Introduction
-
In this age of Industry 4.0, industries are beginning to implement digital integration to boost their production efficiency. According to Anton Setiyawan, Director of Digital Economy Protection of Badan Siber dan Sandi Negara (BSSN) of Indonesia, there will be more Internet of Things (IoT) ecosystem users than the number of smartphones users in Indonesia.
Currently, some of the fruit and vegetable industry in Indonesia are still lacking digital integration and automation for quality checks. By developing a Machine Learning model for fruit / vegetable quality classification in a mobile dashboard, we hope that this will be the first step for the development of IoT implementation in the fruit and vegetable industry, especially in Indonesia.

Repository
-

- [Android](https://github.com/adamramadhn/B21-CAP0354)
- [ML - CC](https://github.com/JurgenStr/B21-CAP0354-ML-CC)

Steps/Code Explanation
-

This app is able to identify a number of fruits and determine its quality with a machine learning model. And then it will insert the data into a database, which will save all the fruit info which is saved into it. Currently the type of fruits are limited to four:
- Apple,
- Banana,
- Lemon, and
- Orange.

And the app will also tell us whether the fruit is fresh or rotten. The similarity percentage we got from the machine learning model will also be identified.

This app works by getting pictures from the user’s gallery, and then sending those pictures to the machine learning model deployed on Virtual Machine Instance with Cloud Computing, where the app will fetch the result of the classification.

Helper dependencies used in this app are:
- Glide: to display images,
- Koin: for dependency injection,
- Lifecycle/ViewModel: for ViewModel,
- Room: for database implementation,
- Retrofit: for networking,
- Sqlcipher, Sqlite: for database security,
- MPAndroidChart: to display charts,
- CircleImageView: to display circle images,
- Firebase: 
  - Authentication
  - Realtime Database
  - Crash Analytics
  - Performance Monitoring
- Picasso: to display images.

There are two main modules in this android project: the App module and the Core module. The app module will contain most of the UI processes and classes needed for the UI to work, while Core will contain most of the background work.

Starting with the Core module, going to folder src/main/java/com/akiramenaide/core, there will be four main package:
- data,
- di,
- domain,
- util.

The app implements Clear Architecture with three main layers. The presentation layer which works on UI is on the app module, data layer which works on storing and fetching data is on package data inside core module, and domain layer which works as a bridge between the two other layers is stored in domain package inside core module.

Package data will contain most processes concerning data flow. The FruitRepository will be stored here, containing functions to get, insert, update fruit data, and get prediction results from an image. There will be another package named source, containing package local and package remote. Each package will have a DataSource. LocalDataSource will work with Room Database, containing Dao Function to insert, update and query fruit data. RemoteDataSource will work with Retrofit to send image data to an address with POST function, then fetch the result. The processes will be done on background thread using runBlocking function to prevent the application from freezing.

Package di contains only a single class named CoreModule. In this class dependency injection (di) will be done with Koin library to implement di on the repository, database and network classes. 

Package domain works as a bridge between data package and UI on app modules. Here the module of fruit data which was an entity on the data package will be converted to a simple Fruit model. There is an interface of the FruitRepository, and also UseCase class which will be used by UI classes, and Interactor class which is linked with UseCase class. 

Last is util package, containing helper classes such as AppExecutor to help run tasks on background and DataMapper to convert the data model.

Going to AppModule, before accessing the app package there will be MyApplication class which will ruin the Koin Module which is used on the application.

Inside UI package, there will be multiple packages:
- auth,
- detail,
- di,
- home,
- main,
- profile,
- splash,
- util.

When the app is ran, SplashActivity inside the splash package will start, showing Lottie animation to greet users. And then the app will go to the login page.

Package auth will contain all of the login and register function on the app. Firebase is used to handle login information, and is utilized to help create other functions such as registering to the app, creating forget password and change password functions. When you logged in onto the app, you won’t have to login anymore next time you access the app. This is done by accessing currentUser on the FirebaseAuth instance.

On the main app, there will only be a single activity, MainActivity. This activity uses BottomNavigationView to navigate between the three main fragments in the app, which is HomeFragment, DetailFragment and ProfileFragment. HomeFragment will display a BarChart showing the total of each fruit data in the database. The BarChart, and all the other charts in the app used a library called MPAndroidChart. The HomeFragment have three button:
Pick Image button,
Predict Image button,
Insert Data button.


The first button will fetch images from the gallery. The predict image button will convert the image as a UTF-8 string, which then will be sent to the HomeViewModel, which is linked with UseCase and will eventually go to the POST function. It will return data containing the class name of the fruit (example = “rottenapple”), and the percentage of its similarity. The insert data button will insert the predicted data into the database.

DetailFragment will get all the fruits data on the database and will first display it on a PieChart, showing the percentage of each fruit’s total in the database. And then there is also a horizontal bar chart dividing the fresh and rotten fruit total of each fruit.

Last is the ProfileFragment. This will work as a personalization page, where users can set their profile picture, name and change email address/password. The personal data and it’s changes will work with firebase. Users can also delete their account in this page, with a warning page using AlertDialog before confirmation. To logout, the MainActivity will have a Logout menu which can log out the user in a single click.

The di package is another dependency injection package. The injection will be used for UseCase and ViewModels, creating modules for one of each. 

Last is the util package. It contains helper classes such as MyColor class to store commonly used color and PredictedObject as a container class for the data fetched from Cloud.

