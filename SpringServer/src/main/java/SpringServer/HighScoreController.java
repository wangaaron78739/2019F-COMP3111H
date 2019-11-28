package SpringServer;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
public class HighScoreController {
  private final List<Integer> highScores = Arrays.asList(0,0,0,0,0,0,0,0,0,0);
  @RequestMapping(value="/highscores",method=RequestMethod.GET)
  @ResponseBody public List<Integer> highScores(@RequestParam(value="newScore",defaultValue="0") int newScore) {
    int score = newScore;
    for(int i=0;i<10;i++) {
      int temp = highScores.get(i);
      if (score > temp) {
        highScores.set(i,score);
        score = temp;
      }
    }
    return highScores;
  }
}
