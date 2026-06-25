package innopolis.tabletennis.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping("/error228")
    public ResponseEntity<String> handleError(HttpServletRequest request, Exception ex) {
        logger.error("Error on /error228 occurred: ", ex);
        return ResponseEntity.internalServerError().body("Sorry. Some error occurred. Contact club leaders please." + ex);
    }

}
