package com.sparta.lafesta.tag.service;

import com.sparta.lafesta.common.dto.ApiResponseDto;
import com.sparta.lafesta.common.exception.UnauthorizedException;
import com.sparta.lafesta.festival.dto.FestivalResponseDto;
import com.sparta.lafesta.festival.entity.Festival;
import com.sparta.lafesta.festival.repository.FestivalRepository;
import com.sparta.lafesta.tag.dto.TagRequestDto;
import com.sparta.lafesta.tag.dto.TagResponseDto;
import com.sparta.lafesta.tag.entity.FestivalTag;
import com.sparta.lafesta.tag.entity.Tag;
import com.sparta.lafesta.tag.repository.FestivalTagRepository;
import com.sparta.lafesta.tag.repository.TagRepository;
import com.sparta.lafesta.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final FestivalTagRepository festivalTagRepository;
    private final FestivalRepository festivalRepository;


    //태그 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<TagResponseDto> selectTags(User user, Pageable pageable) {
        //회원 확인
        if (user == null) {
            throw new IllegalArgumentException("로그인 해주세요");
        }

        return tagRepository.findAllBy(pageable).stream()
                .map(TagResponseDto::new).toList();
    }

    //태그 수정
    @Override
    @Transactional
    public TagResponseDto modifyTag(Long tagId, TagRequestDto requestDto, User user) {
        //관리자만 태그 수정 가능
        if (!user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("관리자만 태그를 수정할 수 있습니다.");
        }

        Tag tag = findTag(tagId);

        tag.modify(requestDto);
        return new TagResponseDto(tag);
    }

    //태그 삭제
    @Override
    @Transactional
    public void deleteTag(Long tagId, User user) {
        //관리자만 태그 삭제 가능
        if (!user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("관리자만 태그를 삭제할 수 있습니다.");
        }

        Tag tag = findTag(tagId);

        tagRepository.delete(tag);
    }

    //페스티벌 태그별 조회
    @Override
    @Transactional(readOnly = true)
    public List<FestivalResponseDto> selectFestivalTags(String title, User user, Pageable pageable) {
        //회원 확인
        if (user == null) {
            throw new IllegalArgumentException("로그인 해주세요");
        }

        Tag tag = tagRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("해당 태그가 없습니다."));

        List<FestivalTag> festivalTags = festivalTagRepository.findAllByTag(tag, pageable);

        List<FestivalResponseDto> tagedFestivals = new ArrayList<>();
        for (FestivalTag festivalTag : festivalTags) {
            Festival tagedFestival = findFestivalByTag(festivalTag);

            FestivalResponseDto festivalResponseDto = new FestivalResponseDto(tagedFestival);
            tagedFestivals.add(festivalResponseDto);
        }
        return tagedFestivals;
    }

    //페스티벌 태그 삭제 - 페스티벌과 맞지 않는 태그를 관리자가 임의로 삭제가능
    @Override
    @Transactional
    public ResponseEntity<ApiResponseDto> deleteFestivalTag(Long festivalId, Long tagId, User user) {
        //관리자만 삭제 가능
        if (!user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            throw new UnauthorizedException("관리자만 태그를 삭제할 수 있습니다.");
        }

        Festival festival = findFestival(festivalId);

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException("해당 태그가 없습니다."));

        //태그 관계 확인
        FestivalTag festivalTag = festivalTagRepository.findByTagAndFestival(tag, festival)
                .orElseThrow(() -> new IllegalArgumentException("태그 되어있지 않습니다."));

        festivalTagRepository.delete(festivalTag);

        //사용되지 않는 태그는 삭제
        deleteUnusedTag(tag);

        return ResponseEntity.ok().body(new ApiResponseDto(HttpStatus.OK.value(),
                "'" + tag.getTitle() + "' 태그를 '" + festival.getTitle() + "'에서 제외했습니다."));
    }


    //태그 id로 태그 찾기
    private Tag findTag(Long tagId) {
        return tagRepository.findById(tagId).orElseThrow(() ->
                new IllegalArgumentException("선택한 태그는 존재하지 않습니다."));
    }


    //존재하는 태그인지 확인 -> 없으면 생성 / 존재하는 태그면 가져오기
    @Override
    public Tag checkTag(TagRequestDto requestDto) {
        Optional<Tag> checkTag = tagRepository.findByTitle(requestDto.getTitle());
        //이미 존재하는 태그인지 확인
        if (checkTag.isPresent()) {
            return checkTag
                    .orElseThrow(() -> new IllegalArgumentException("해당 태그가 이미 존재합니다."));
        } else {
            //태그 생성
            Tag tag = new Tag(requestDto);
            return tagRepository.save(tag);
        }
    }

    //태그 중복 확인 & 태그 페스티벌 연관관계 생성
    public void connectTag(Festival festival, Tag tag) {
        //중복 확인
        if (festivalTagRepository.findByTagAndFestival(tag, festival).isPresent()) {
            throw new IllegalArgumentException("중복되는 태그입니다.");
        }
        //연관관계 생성
        festivalTagRepository.save(new FestivalTag(tag, festival));
        return;
    }

    //id로 페스티벌 가져오기
    private Festival findFestival(Long festivalId) {
        return festivalRepository.findById(festivalId).orElseThrow(() ->
                new IllegalArgumentException("선택한 페스티벌은 존재하지 않습니다.")
        );
    }

    //페스티벌 태그로 페스티벌 찾기
    private Festival findFestivalByTag(FestivalTag festivalTag) {
        return festivalRepository.findByTags(festivalTag)
                .orElseThrow(() -> new IllegalArgumentException("해당 페스티벌이 없습니다."));
    }

    //페스티벌로 페스티벌 태그 연관관계 찾기
    public List<FestivalTag> findFestivalTagsByFestival(Festival festival) {
        return festivalTagRepository.findAllByFestival(festival);
    }

    //페스티벌 태그 삭제
    public void deleteFestivalTag(FestivalTag festivalTag) {
        festivalTagRepository.delete(festivalTag);
    }

    //사용되지 않는 태그는 삭제
    public void deleteUnusedTag(Tag tag) {
        List<FestivalTag> usedTag = festivalTagRepository.findAllByTag(tag);
        if (usedTag.isEmpty()) {
            tagRepository.delete(tag);
        }
    }

    @Override
    public Tag findTagByTitle (String title) {
        return tagRepository.findByTitle(title).orElseThrow(() ->
                new IllegalArgumentException("선택한 태그는 존재하지 않습니다.")
        );
    }
}
