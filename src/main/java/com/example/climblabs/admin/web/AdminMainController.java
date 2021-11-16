package com.example.climblabs.admin.web;

import com.example.climblabs.admin.web.dto.AdminPostResponse;
import com.example.climblabs.admin.web.dto.AdminSearchInput;
import com.example.climblabs.admin.web.dto.PostCreateInput;
import com.example.climblabs.global.utils.common.RatingCreate;
import com.example.climblabs.post.service.PostService;
import com.example.climblabs.post.web.dto.PostRequest;
import com.example.climblabs.post.web.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String search(AdminSearchInput request, Model model) {

        List<PostResponse> postResponses = postService.findSearchPost(request.getSearchType(), request.getSearchValue(),
                PageRequest.of(0, 3));
        model.addAttribute("posts", postResponses);
        return "admin/post/list";
    }

    @GetMapping("/admin/posts/create.do")
    public String createPost(Model model) {

        model.addAttribute("ratings", RatingCreate.getRatings());
        return "admin/post/add";
    }

    @PostMapping("/admin/posts/create.do")
    @ResponseBody
    public boolean createPostSubmit(PostRequest request) {

        long newPostId = postService.createNewPost(request);
        if (newPostId == 0) {
            return false;
        }
        return true;
    }
}
