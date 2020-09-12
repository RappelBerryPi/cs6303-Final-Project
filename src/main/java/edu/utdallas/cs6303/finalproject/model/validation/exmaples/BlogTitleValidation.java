//import javax.validation.ConstraintValidator;
//import javax.validation.ConstraintValidatorContext;

//import org.imperfectmommy.model.database.repositories.BlogPostRepository;

//public class BlogTitleValidation implements ConstraintValidator<BlogTitleIsUnique, String> {

    //private BlogPostRepository blogPostRepository;

    //public BlogTitleValidation(BlogPostRepository blogPostRepository) {
        //this.blogPostRepository = blogPostRepository;
    //}

    //@Override
    //public boolean isValid(String value, ConstraintValidatorContext context) {
        //if (blogPostRepository.findByTitle(value).isEmpty()) {
            //return true;
        //}
        //return false;
    //}

//}