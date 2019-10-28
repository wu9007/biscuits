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

## 2.Route And Authority Control (under the package called `controller`)
#### Create a public route holding class named `HomeContoller.java` as follows:

```java
import org.hv.biscuits.annotation.Action;
import org.hv.biscuits.annotation.Controller;
import org.hv.biscuits.controller.AbstractController;

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
import org.hv.biscuits.annotation.Action;
import org.hv.biscuits.annotation.Auth;
import org.hv.biscuits.annotation.Controller;
import org.hv.biscuits.controller.AbstractController;

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

## 3.Register Bundle And Authority (under the package called `demo`)
#### Create a class for the app to register `bundles` named `DemoBundleRegister.java` as follows:
```java
import org.hv.biscuits.bundle.AbstractBundle;

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
import org.hv.biscuits.permission.AbstractPermission;

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

> So far, you can send a GET request with the URL 'http://localhost:8080/home/welcome' to the program.<br/>
But you cannot access the URL 'http://localhost:8080/demo/read_test' and 'http://localhost:8080/demo/write_test' temporarily because of you are not granted the permission named "demo_read".

> Next, I will show you how to read and write the database to achieve the registration and login functions of the program, and then authorize the user.

## 3.Read Write Database (under the package called `repository`)
#### Create a interface called `UserRepository.java` under the package called `repository`
```java
import org.hv.biscuits.repository.CommonRepository;
import org.hv.biscuits.spine.model.User;

public interface UserRepository extends CommonRepository<User> {

    User findByAvatarAndPassword(String avatar, String password) throws SQLException;
}
```
#### Create a class named `UserRepositoryImpl.java` which implement `UserRepository.java` under the package called `repository.impl`
```java
import org.hv.biscuits.repository.AbstractCommonRepository;
import org.hv.biscuits.spine.model.User;
import org.hv.pocket.criteria.Criteria;
import org.hv.pocket.criteria.Restrictions;

@Component
public class UserRepositoryImpl extends AbstractCommonRepository<User> implements UserRepository {
    @Override
    public User findByAvatarAndPassword(String avatar, String password) throws SQLException {
        Criteria criteria = super.getSession().createCriteria(User.class);
        criteria.add(Restrictions.equ("avatar", avatar))
                .add(Restrictions.equ("password", password));
        return (User) criteria.unique();
    }
}
```
> `org.hv.biscuits.spine.model.User` is the bean that the author has created to map with the database.

## 5.Serving For Controller (under the package named `service`)
#### Create a interface called `UserService.java` under the package called `service`
```java
public interface UserService {

    String generateToken(String avatar, String password) throws SQLException;

    User register(String avatar, String name, String password) throws SQLException, IllegalAccessException;
}
```
#### Create a class named `UserServiceImpl.java` which implement `UserService.java` under the package called `service.impl`
```java
import org.hv.biscuits.annotation.Service;
import org.hv.biscuits.service.AbstractService;
import org.hv.biscuits.spine.model.User;
import org.hv.demo.service.UserService;
import org.hv.biscuits.spine.utils.EncodeUtil;
import org.hv.biscuits.utils.TokenUtil;
import org.hv.demo.repository.UserRepository;

@Service(session = "demo")
public class UserServiceImpl extends AbstractService implements UserService {
    private final TokenUtil tokenUtil;
    private final EncodeUtil encodeUtil;
    private final UserRepository userRepository;

    public UserServiceImpl(TokenUtil tokenUtil, EncodeUtil encodeUtil, UserRepository userRepository) {
        this.tokenUtil = tokenUtil;
        this.encodeUtil = encodeUtil;
        this.userRepository = userRepository;
    }

    @Override
    public String generateToken(String avatar, String password) throws SQLException {
        User user = this.userRepository.findByAvatarAndPassword(avatar, this.encodeUtil.abcEncoder(password));
        if (user == null) {
            throw new IllegalArgumentException("The username or password is incorrect. Please fill in again.");
        }
        return this.tokenUtil.generateToken(user);
    }

    @Override
    public User register(String avatar, String name, String password) throws SQLException, IllegalAccessException {
        User user = new User();
        user.setAvatar(avatar);
        user.setName(name);
        user.setEnable(true);
        user.setPassword(encodeUtil.abcEncoder(password));
        user.setLastPasswordResetDate(new Date());
        user.setLastRoleModifyDate(new Date());
        this.userRepository.save(user, false);
        return user;
    }
}
```

> At the same time you need add a session named `demo` in application.yml `session: biscuits,demo`

## 6.Register And Login (under the package named `controller`)
```java
@Controller(bundleId = "guest", auth = false)
public class GuestController extends AbstractController {
    private final UserService userService;

    public GuestController(UserService userService) {
        this.userService = userService;
    }

    @Action(actionId = "login", method = RequestMethod.POST)
    public String login(@RequestParam String avatar, @RequestParam String password) throws SQLException {
        return userService.generateToken(avatar, password);
    }

    @Action(actionId = "register", method = RequestMethod.POST)
    public Body<User> register(@RequestParam String avatar, @RequestParam String name, @RequestParam String password) throws SQLException, IllegalAccessException {
        if (avatar == null || password == null || name == null) {
            return Body.newWaringInstance("Fail", "Please fill in the information correctly.", null);
        } else {
            User user = this.userService.register(avatar, name, password);
            return Body.newSuccessInstance("Success", "Congratulations! Registration succeeded!", user);
        }
    }
}
```
