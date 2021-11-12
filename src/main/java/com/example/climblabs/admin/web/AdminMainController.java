package com.example.climblabs.admin.web;

import com.example.climblabs.admin.web.dto.AdminPostResponse;
import com.example.climblabs.admin.web.dto.AdminSearchInput;
import com.example.climblabs.admin.web.dto.PostCreateInput;
import com.example.climblabs.post.service.PostService;
import com.example.climblabs.post.web.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AdminMainController {

    private final PostService postService;

    @GetMapping("/admin")
    public String adminPage() {
        return "admin/admin_main_view";
    }

    @GetMapping("/admin/posts")
    public String postPage(@PageableDefault Pageable pageable, Model model) {

        List<PostResponse> postResponses = postService.readPost(PageRequest.of(0, 3));

        model.addAttribute("posts", postResponses);


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

    @GetMapping("/admin/posts/create.do")
    public String createPost(Model model) {

        model.addAttribute("postInfo", new AdminPostResponse());
        model.addAttribute("ratings", new LinkedHashMap<Integer, String>() {
            {
                put(5, "★★★★★");
                put(4, "★★★★");
                put(3, "★★★");
                put(2, "★★");
                put(1, "★");
            }
        });
        return "admin/post/add";
    }

    @PostMapping("/admin/posts/create.do")
    @ResponseBody
    public boolean createPostSubmit(PostCreateInput postCreateInput) {

        log.info(""+postCreateInput.getLevel());
        log.info(postCreateInput.getClimbingTitle());
        log.info(postCreateInput.getFeature());
        log.info(postCreateInput.getAddress());
        log.info(postCreateInput.getZipCode());
        log.info(postCreateInput.getDetailAddress());
        log.info(postCreateInput.getSize());
        log.info(postCreateInput.getTitle());

        return true;
    }
}
