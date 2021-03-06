package parenkov.tests;

import com.codeborne.selenide.Condition;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.is;

public class WebShopTests extends TestBase {

    @Test
    @DisplayName("User registration")
    void registration() {
        step("Register user by API", () -> {
        given()
                .contentType("application/x-www-form-urlencoded")
                .body("__RequestVerificationToken=yharMk0UzLqakit" +
                        "-MPnBjOJc6oyYNnsn8u_CWOwbVk9djHYwKWUFoxe" +
                        "0_2nndWrMgtJaual3dxIZ6SkidpixmRpzv6NucA-" +
                        "EDDWc0k5Wmrk1&Gender=M&FirstName=Alex&La" +
                        "stName=Qwerty&Email=qwerty%40www.co&Pass" +
                        "word=123456&ConfirmPassword=123456&regis" +
                        "ter-button=Register")
                .when()
                .post("/register")
                .then()
                .statusCode(302);
        get("/registerresult/1")
                .then()
                .statusCode(200);
        });

        step("Check that registration successed by UI", () -> {
        open("/registerresult/1");
        $(".result").shouldHave(Condition.text("Your registration completed"));
        });
    }

    @Test
    @DisplayName("Adding an item to the Shopping Cart")
    void addItemToShoppingCart() {
        step("Add an item with custom specs to the Shopping Cart by API", () -> {
        Response response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("product_attribute_74_5_26=82" +
                                "&product_attribute_74_6_27=85" +
                                "&product_attribute_74_3_28=87" +
                                "&product_attribute_74_8_29=88" +
                                "&product_attribute_74_8_29=89" +
                                "&product_attribute_74_8_29=90" +
                                "&addtocart_74.EnteredQuantity=2")
                        .cookie("Nop.customer=69589107-6373-41bd-891d-47fb44277adc;")
                        .when()
                        .post("/addproducttocart/details/74/1")
                        .then()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is("The product has been added to your " +
                                "<a href=\"/cart\">shopping cart</a>"))
                        .extract().
                        response();
        System.out.println("Quantity: " + response.path("updatetopcartsectionhtml"));
        System.out.println(response.asString());
        });

        step("Set cookies to browser", () -> {
        open("/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(
                new Cookie("Nop.customer", "69589107-6373-41bd-891d-47fb44277adc"));
        });

        step("Check that item has been added to Shopping Cart by UI", () -> {
        open("/cart");
        $(".cart-item-row").shouldBe(Condition.visible);
        $(".product-name").shouldHave(Condition.text("Build your own expensive computer"));
        });
    }

    @Test
    @DisplayName("Sending feedback by 'Contact Us' form")
    void leaveFeedback() {
        step("Fill the 'Contact Us' form and send feedback by API", () -> {
        given()
                .contentType("application/x-www-form-urlencoded")
                .body("FullName=Alex+Qwerty&Email=qwerty%40www.co" +
                        "&Enquiry=Test+enquiry&send-email=Submit")
                .cookie("Nop.customer=69589107-6373-41bd-891d-47fb44277adc;")
                .when()
                .post("/contactus")
                .then()
                .statusCode(200);
        });
    }
}






