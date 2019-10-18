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
> This route can be accessed without any permission, because the `auth` attribute in the `@Controller` annotation has been set to `false`.

```java
@Controller(bundleId = "home", auth = false)
public class HomeController extends AbstractController {

    @Action(actionId = "welcome", method = RequestMethod.GET)
    public String welcome() {
        return "welcome to biscuits!";
    }
}
```

### 3.Register Bundle
> At the same time, you need to manually register the `bunldeId` attribute value `home` in the annotation of the `controller` class you created in the previous step into the app.
#### Create a class for the app to register `bundles` named `DemoBundleRegister. java` as follows:
```java
@Component
public class DemoBundleRegister extends AbstractBundle {

    @Override
    public void init() {
        // you can register all bundles in the app at this place.
        this.register("home", "Home-Bundle");
    }
}
```