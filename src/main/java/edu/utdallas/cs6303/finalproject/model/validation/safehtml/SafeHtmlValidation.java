package edu.utdallas.cs6303.finalproject.model.validation.safehtml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

public class SafeHtmlValidation implements ConstraintValidator<SafeHtml, String> {

    private List<String> allowedTags;
    private Whitelist    whitelist;

    @Override
    public void initialize(SafeHtml safeHtml) {
        allowedTags = new ArrayList<>();
        Collections.addAll(allowedTags,
                new String[] { "a", "b", "blockquote", "br", "cite", "code", "dd", "dl", "dt", "em", "i", "li", "ol", "p", "pre", "q", "small", "span", "strike", "strong", "sub", "sup", "u", "ul" });
        Collections.addAll(allowedTags, safeHtml.allowedTags());
        Predicate<String> p = String::isBlank;
        p.negate();
        allowedTags    = allowedTags.stream().filter(p).map(String::toLowerCase).collect(Collectors.toList());
        this.whitelist = Whitelist.basic();
        this.whitelist.addTags(allowedTags.toArray(String[]::new));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.isBlank()) {
            return true;
        }
        boolean isValid = Jsoup.isValid(value, this.whitelist);
        if (!isValid) {
            List<String> invalidElementsList = new ArrayList<>();
            Element      bodyElement         = Jsoup.parse(value).body();
            bodyElement.getAllElements()
                       .stream()
                       .map(e -> e.tagName().toLowerCase())
                       .filter(s -> !allowedTags.contains(s))
                       .forEach(s -> invalidElementsList.add(s));
            String invalidTagsString = "Invalid Tags: " + String.join(",", invalidElementsList);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(invalidTagsString).addConstraintViolation();
        }
        return isValid;
    }

}