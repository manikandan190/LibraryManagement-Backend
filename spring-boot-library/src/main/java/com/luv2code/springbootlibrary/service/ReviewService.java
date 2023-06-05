package com.luv2code.springbootlibrary.service;

import com.luv2code.springbootlibrary.deo.BookRepository;
import com.luv2code.springbootlibrary.deo.ReviewRespository;
import com.luv2code.springbootlibrary.entity.Review;
import com.luv2code.springbootlibrary.requestmodels.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
public class ReviewService {

    private ReviewRespository reviewRepository;
    @Autowired
    public ReviewService(ReviewRespository reviewRespository){
        this.reviewRepository=reviewRespository;
    }
    public void postReview(String userEmail, ReviewRequest reviewRequest) throws Exception{
        Review validateReview=reviewRepository.findByUserEmailAndBookId(userEmail,reviewRequest.getBookId());
    if(validateReview!=null){
        throw new Exception("Review already created");
    }
    Review review=new Review();
    review.setBookId(reviewRequest.getBookId());
    review.setRating(reviewRequest.getRating());
    review.setUserEmail(userEmail);
    if(reviewRequest.getReviewDescription().isPresent())
    {
        review.setReviewDescription(reviewRequest.getReviewDescription().map(
                Object::toString
        ).orElse(null));
    }
        LocalDate localDate = LocalDate.now();
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        review.setDate(date);
    reviewRepository.save(review);
}
public Boolean userReviewListed(String userEmail,Long bookId)
{
    Review validateReview=reviewRepository.findByUserEmailAndBookId(userEmail,bookId);
    if(validateReview!=null)
    {
        return true;
    }
    else {
        return false;
    }

}
}
