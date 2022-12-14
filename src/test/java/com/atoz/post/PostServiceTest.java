package com.atoz.post;

import com.atoz.post.dto.request.AddPostRequestDto;
import com.atoz.post.helper.SpyPostMapper;
import com.atoz.security.authentication.helper.CustomWithMockUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class PostServiceTest {

    private SpyPostMapper postMapper = new SpyPostMapper();

    private PostService sut = new PostServiceImpl(postMapper);
    private static final String USER_ID = "testUserId";

    @Test
    @CustomWithMockUser(username = USER_ID)
    void addPost_게시글을_추가한_사용자_아이디가_저장된다() {
        AddPostRequestDto addPostRequestDto = AddPostRequestDto.builder()
                .title("testTitle")
                .content("testContent")
                .build();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        sut.addPost(addPostRequestDto, userDetails);


        assertEquals(USER_ID, postMapper.receivedUserId);
    }
}
