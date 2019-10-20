# 🗺 Write Your First App
## 1.Directory Structure
#### Make the directory structure of your project as follows:
```text
└── com
    └── example
        └── demo
            ├── controller
            ├── service
                └── impl
            ├── repository
                └── impl
            └── model
```

## 2.Route And Authority Control
#### Create a public route holding class named `HomeContoller.java` as follows:

```java
// This route can be accessed without any permission, 
// because the `auth` attribute in the `@Controller` annotation has been set to `false`.
@Controller(bundleId = "home", auth = false)
public class HomeController extends AbstractController {

    @Action(actionId = "welcome", method = RequestMethod.GET)
    public String welcome() {
        return "welcome to biscuits!";
    }
}
```

#### Create a route holding class named `DemoController.java` as follows that can only be accessed with corresponding permissions:
```java
@Controller(bundleId = "demo", auth = true)
public class OrderController extends AbstractController {

    @Auth(value = "demo_read")
    @Action(actionId = "read_test", method = RequestMethod.GET)
    public String readTest(){
        return "welcome to demo with demo_read auth!";
    }
    
    @Auth(value = "demo_write")
    @Action(actionId = "write_test", method = RequestMethod.GET)
    public String writeTest(){
        return "welcome to demo with demo_write auth!";
    }
}
```

> At the same time, you need to manually register the `bunldeId` and `authId` into this app.

## 3.Register Bundle And Authority
#### Create a class for the app to register `bundles` named `DemoBundleRegister.java` as follows:
```java
@Component
public class DemoBundleRegister extends AbstractBundle {

    @Override
    public void init() {
        // you can register all bundles in the app at this place.
        this.register("home", "Home-Bundle");
        this.register("demo", "Demo-Bundle");
    }
}
```
#### Create a class for the app to register `authority` named `DemoAuthRegister.java` as follows:
```java
@Component
public class DemoAuthRegister extends AbstractPermission {

    @Override
    public void init() {
        // you can register all permissions in the app at this place.
        super.register("demo", "demo_read", "Read", "read information from demo.");
        super.register("demo", "demo_write", "Write", "write into demo.");
    }
}
```

> So far, you can use HTTP to send a GET request with the URL 'http://localhost:8080/home/welcome' to the program.<br/>
But you cannot access the URL 'http://localhost:8080/demo/read_test' and 'http://localhost:8080/demo/write_test' temporarily because of you are not granted the permission named "demo_read".