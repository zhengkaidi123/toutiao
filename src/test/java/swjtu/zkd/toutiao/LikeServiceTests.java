package swjtu.zkd.toutiao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import swjtu.zkd.toutiao.service.LikeService;

@SpringBootTest(classes = ToutiaoApplication.class)
public class LikeServiceTests {

    @Autowired
    LikeService likeService;

    @Test
    void testLike() {
        likeService.like(12, 1, 1);
        Assertions.assertEquals(1, likeService.getLikeStatus(12, 1, 1));

        likeService.dislike(12, 1, 1);
        Assertions.assertEquals(-1, likeService.getLikeStatus(12, 1, 1));
    }

    @Test
    void testB() {
        System.out.println("B");
    }

    @Test
    void testException() {
        throw new IllegalArgumentException("exception");
    }

    @BeforeEach
    void setUp() {
        System.out.println("setUp");
    }

    @AfterEach
    void tearDown() {
        System.out.println("tearDown");
    }

    @BeforeAll
    static void beforeClass() {
        System.out.println("beforeClass");
    }

    @AfterAll
    static void afterClass() {
        System.out.println("afterClass");
    }

}
