package se.sowl.stitchapi.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.StudyPostException;
import se.sowl.stitchapi.study.dto.request.StudyPostCommentRequest;
import se.sowl.stitchapi.exception.UserCamInfoException;
import se.sowl.stitchapi.study.dto.request.StudyPostRequest;
import se.sowl.stitchapi.study.dto.response.StudyPostCommentResponse;
import se.sowl.stitchdomain.study.domain.StudyPost;
import se.sowl.stitchdomain.study.domain.StudyPostComment;
import se.sowl.stitchdomain.study.repository.StudyPostCommentRepository;
import se.sowl.stitchdomain.study.repository.StudyPostRepository;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyPostCommentService {

    private final StudyPostCommentRepository studyPostCommentRepository;
    private final StudyPostRepository studyPostRepository;
    private final UserCamInfoRepository userCamInfoRepository;

    @Transactional
    public List<StudyPostCommentResponse> getCommentsByPostId(Long studyPostId){
        StudyPost studyPost = studyPostRepository.findById(studyPostId)
                .orElseThrow(StudyPostException.StudyPostNotFoundException::new);

        List<StudyPostComment> comments = studyPostCommentRepository.findByStudyPostId(studyPost.getId());

        return comments.stream()
                .map(StudyPostCommentResponse::from)
                .toList();
    }

    @Transactional
    public StudyPostCommentResponse createStudyComments(StudyPostCommentRequest request, Long userCamInfoId){
        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        StudyPost studyPost = studyPostRepository.findById(request.getStudyPostId())
                .orElseThrow(StudyPostException.StudyPostNotFoundException::new);

        StudyPostComment studyPostComment = StudyPostComment.builder()
                .studyPost(studyPost)
                .userCamInfo(userCamInfo)
                .content(request.getContent())
                .build();

        studyPostCommentRepository.save(studyPostComment);
        return StudyPostCommentResponse.from(studyPostComment);
    }

    @Transactional
    public StudyPostCommentResponse updateStudyPostComment(Long commentId, StudyPostCommentRequest request, Long userCamInfoId){
        StudyPostComment comment = studyPostCommentRepository.findById(commentId)
                .orElseThrow(StudyPostException.StudyPostCommentNotFoundException::new);

        if (!comment.getUserCamInfo().getId().equals(userCamInfoId)){
            throw new StudyPostException.UnauthorizedException();
        }

        comment.updateContent(request.getContent());

        return StudyPostCommentResponse.from(comment);
    }

    @Transactional
    public void deleteStudyPostComment(Long commentId, Long userCamInfoId){
        StudyPostComment comment = studyPostCommentRepository.findById(commentId)
                .orElseThrow(StudyPostException.StudyPostCommentNotFoundException::new);

        if (!comment.getUserCamInfo().getId().equals(userCamInfoId)){
            throw new StudyPostException.UnauthorizedException();
        }

        studyPostCommentRepository.delete(comment);
    }
}
