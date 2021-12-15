package com.example.climblabs.post.domain;

import com.example.climblabs.common.factory.MemberFactory;
import com.example.climblabs.global.utils.image.dto.ImageFileDto;
import com.example.climblabs.member.domain.Member;
import com.example.climblabs.member.domain.repository.MemberRepository;
import com.example.climblabs.post.domain.Image.Image;
import com.example.climblabs.post.domain.content.Advantage;
import com.example.climblabs.post.domain.content.DisAdvantage;
import com.example.climblabs.post.domain.repository.AdvantageRepository;
import com.example.climblabs.post.domain.repository.DisAdvantageRepository;
import com.example.climblabs.post.domain.repository.ImageRepository;
import com.example.climblabs.post.domain.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AdvantageRepository advantageRepository;

    @Autowired
    private DisAdvantageRepository disAdvantageRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Test
    @DisplayName("게시물을 등록에 성공한다.")
    public void addPost() {

        //given
        Member member = MemberFactory.createMember("뉴맨", "홍길동");
        memberRepository.save(member);

        Post post = Post.builder()
                .title("암장이름")
                .level(1)
                .scaleType(ScaleType.BIG)
                .feature("특징")
                .build();

        post.setMember(member);
        //when
        Post savedPost = postRepository.save(post);
        //then
        assertThat(savedPost).isEqualTo(postRepository.findById(post.getId()).orElse(null));
    }

    @Test
    @DisplayName("게시물에 장점 2개 저장을 성공한다.")
    public void addAdvantagesInPost() {
        //given
        Member member = MemberFactory.createMember("뉴맨", "홍길동");
        memberRepository.save(member);

        Post post = Post.builder().build();

        post.setMember(member);
        postRepository.save(post);

        Advantage advantage1 = Advantage.builder()
                .item("장점1")
                .build();

        Advantage advantage2 = Advantage.builder()
                .item("장점2")
                .build();

        advantage1.setPost(post);
        advantage2.setPost(post);

        //when
        Advantage saved1 = advantageRepository.save(advantage1);
        Advantage saved2 = advantageRepository.save(advantage2);

        //then
        assertThat(post.getAdvantages()).hasSize(2);
        assertThat(post.getAdvantages()).containsOnly(saved1, saved2);
    }

    @Test
    @DisplayName("게시물에 단점 2개를 저장한다.")
    public void addDisAdvantagesInPost() {

        //given
        Post post = Post.builder().build();
        postRepository.save(post);

        DisAdvantage disAdvantage1 = DisAdvantage.builder()
                .item("단점1")
                .build();

        DisAdvantage disAdvantage2 = DisAdvantage.builder()
                .item("단점2")
                .build();

        disAdvantage1.setPost(post);
        disAdvantage2.setPost(post);
        //when
        DisAdvantage saved1 = disAdvantageRepository.save(disAdvantage1);
        DisAdvantage saved2 = disAdvantageRepository.save(disAdvantage2);

        //then
        assertThat(post.getDisAdvantages()).hasSize(2);
        assertThat(post.getDisAdvantages()).containsOnly(saved1, saved2);
    }

    @Test
    @DisplayName("게시물에 이미지 2개를 저장한다.")
    public void addImageInPost() {
        //given
        Post post = Post.builder().build();
        Post savedPost = postRepository.save(post);

        ImageFileDto imageFileDto1 = ImageFileDto.builder().name("image1").url("http://image1.png").build();
        ImageFileDto imageFileDto2 = ImageFileDto.builder().name("image2").url("http://image2.png").build();

        Image image1 = Image.builder().imageFileDto(imageFileDto1).build();
        Image image2 = Image.builder().imageFileDto(imageFileDto2).build();

        image1.setPost(post);
        image2.setPost(post);

        //when
        Image saved1 = imageRepository.save(image1);
        Image saved2 = imageRepository.save(image2);

        //then
        assertThat(savedPost.getImages()).hasSize(2);
        assertThat(savedPost.getImages()).containsOnly(saved1, saved2);
    }
}
