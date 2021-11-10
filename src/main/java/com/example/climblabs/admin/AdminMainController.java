package com.example.climblabs.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminMainController {

    @GetMapping("/admin")
    public String adminPage() {
        return "admin/admin_main_view";
    }

    @GetMapping("/admin/posts")
    public String postPage(Model model) {
        List<PostTest> posts = new ArrayList<>();
        posts.add(new PostTest("test1", "1111"));
        posts.add(new PostTest("test2", "2222"));
        posts.add(new PostTest("test3", "3333"));
        posts.add(new PostTest("test3", "3333"));
        posts.add(new PostTest("test3", "3333"));
        posts.add(new PostTest("test3", "3333"));
        posts.add(new PostTest("test3", "3333"));
        posts.add(new PostTest("test3", "3333"));

        model.addAttribute("posts", posts);

        return "admin/post/list";
    }

    @GetMapping("/admin/settings")
    public String settingsView() {
        return "admin/setting";
    }

    @GetMapping("/admin/posts/search.do")
    public String search(AdminSearchInput input) {
        log.info(input.getSearch());

        return "admin/admin_main_view";
    }

    @Getter
    @AllArgsConstructor
    public class PostTest {
        private String name;
        private String price;
    }
}
