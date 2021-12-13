package com.example.climblabs.admin.web;

import com.example.climblabs.admin.web.dto.CommonRequestDto;
import com.example.climblabs.global.utils.common.RatingCreate;
import com.example.climblabs.post.service.PostService;
import com.example.climblabs.post.web.dto.request.PostRequest;
import com.example.climblabs.post.web.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public String postPage(@ModelAttribute("params") PostRequest request, Model model) {

        List<PostResponse> postResponses = postService.readPost(request);
        model.addAttribute("posts", postResponses);

        return "admin/post/list";
    }

    @GetMapping("/admin/settings")
    public String settingsView() {
        return "admin/setting";
    }

    @GetMapping("/admin/posts/search.do")
    public String search(@ModelAttribute("params") CommonRequestDto request, Model model) {

        List<PostResponse> postResponses = postService.findSearchPost(request);
        model.addAttribute("posts", postResponses);
        return "admin/post/list";
    }

    @GetMapping(value = {"/admin/posts/create.do", "/admin/posts/update.do"})
    public String createPost(@RequestParam(value = "id", required = false) Long postId, HttpServletRequest request, Model model) {

        if (request.getRequestURI().contains("update.do")) {
            PostResponse updatedPostResponse = postService.findByIdPost(postId);
            log.info(""+ postId);
            model.addAttribute("post", updatedPostResponse);
        }
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
