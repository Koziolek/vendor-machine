package pl.koziolekweb.vendormachine.utils;

import io.vavr.control.Either;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiHelper {

    public static <L, R> ResponseEntity<?> asResponse(Either<ResponseEntity<L>, ResponseEntity<R>> result){
        if (result.isRight())
            return result.get();
        return result.getLeft();
    }
}
