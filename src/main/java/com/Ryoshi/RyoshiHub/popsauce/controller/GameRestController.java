package com.Ryoshi.RyoshiHub.popsauce.controller;

import com.Ryoshi.RyoshiHub.popsauce.model.Guess;
import com.Ryoshi.RyoshiHub.popsauce.repository.GuessRepository;
import com.Ryoshi.RyoshiHub.popsauce.service.*;
import com.google.gson.Gson;
import com.Ryoshi.RyoshiHub.popsauce.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/popsauce")
@RequiredArgsConstructor
public class GameRestController {

    private final PictureService pictureService;
    private final GameService gameService;
    private final PlayerService playerService;
    private final GuessRepository guessRepository;

    @GetMapping("/is-code-valid/{code}")
    public boolean isCodeValid(@PathVariable String code){
        Game game = gameService.getByCode(code);
        return game != null;
    }

    @GetMapping("/is-username-valid/{username}")
    public boolean isUsernameValid(@PathVariable String username){
        Player player = playerService.findByUsername(username);
        return player == null;
    }

    @GetMapping("/get-current-picture/{code}")
    public String getCurrentPicture(@PathVariable String code){
        Game game = gameService.getByCode(code);
        Picture currentPicture = gameService.getCurrentPictureOfGame(game);
        Gson gson = new Gson();
        return gson.toJson(currentPicture);
    }

    @PostMapping("/create")
    public String createGame(@RequestBody @NonNull Game game){
        Player host = game.getHost();
        host.setPoints(0);

        //Make a Code
        String[] alphabet = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        StringBuilder code = new StringBuilder();
        do{
            for (int i = 0;i<4;i++){
                code.append(alphabet[(int) (Math.random()*(alphabet.length))]);
            }
        }while (gameService.containsCode(code.toString()));
        playerService.save(host);

        //Set Up Game
        game.setCode(code.toString());
        game.setCurrentTimer(0);
        game.setCurrentPicture(0);
        gameService.save(game);

        List<Picture> pictures = pictureService.findAllByCategory(game.getSetting().getCategory());
        //Shuffle The list
        for (int i = 0; i < pictures.size(); i++){
            Picture first = pictures.get(i);
            int random = (int) (Math.floor(Math.random() * pictures.size()));
            pictures.set(i, pictures.get(random));
            pictures.set(random, first);
        }
        //Insert List
        for (Picture picture : pictures) {
            gameService.addPictureToGame(game, picture);
        }

        //Set Up the host
        gameService.addPlayerToGame(game, host);
        playerService.save(host);
        return code.toString();
    }

    @GetMapping("/getAllPlayer/{code}")
    private String getAllPlayer(@PathVariable String code){
        Game game = gameService.getByCode(code);
        List<Player> players = gameService.getAllPlayersByGame(game);
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (Player player : players) {
            json.append("{\"username\":\"").append(player.getUsername()).append("\",\"points\":").append(player.getPoints()).append("},");
        }
        json.deleteCharAt(json.length()-1);
        json.append("]");
        return json.toString();
    }

    @GetMapping("/is-started/{code}")
    public String isStarted(@PathVariable String code){
        Game game = gameService.getByCode(code);
        return String.valueOf(gameService.isStarted(game));
    }

    @GetMapping("/get-host/{code}")
    public String getHost(@PathVariable String code){
        return gameService.getByCode(code).getHost().getUsername();
    }

    @GetMapping("/get-current-timer/{code}")
    public String getCurrentGameTimer(@PathVariable String code){
        Game game = gameService.getByCode(code);
        return String.valueOf(game.getCurrentTimer());
    }

    @GetMapping("/download-flag-data")
    public void downloadFlagData(){
        String[] flagNames  = {"af","eg","ax","al","dz","as","vi","ad","ao","ai","aq","ag","gq","ar","am",
                "aw","az","et","au","bs","bh","bd","bb","be","bz","bj","bm","bt","bo","ba","bw","bv","br",
                "vg","io","bn","bg","bf","bi","cl","cn","ck","cr","cw","dk","de","dm","do","dj","ec","sv",
                "ci","gb-eng","er","ee","fk","fo","fj","fi","fr","gf","pf","tf","ga","gm","ge","gh","gi",
                "gd","gr","gl","gp","gu","gt","gg","gn","gw","gy","ht","hm","hn","hk","in","id","im","iq",
                "ir","ie","is","il","it","jm","jp","ye","je","jo","ky","kh","cm","ca","cv","bq","kz","qa",
                "ke","kg","ki","um","cc","co","km","cg","cd","xk","hr","cu","kw","la","ls","lv","lb","lr",
                "ly","li","lt","lu","mo","mg","mw","my","mv","ml","mt","ma","mh","mq","mr","mu","yt","mx",
                "fm","md","mc","mn","me","ms","mz","mm", "na","nr","nc", "nz","ni","nl","ne","ng","nu",
                "gb-nir","kp","mp","mk","nf","no","om", "at","tl","pk","ps","pw","pa","pg","py","pe","ph",
                "pn","pl","pt","pr","re","rw","ro","ru","bl","mf","sb","zm","ws","sm","st","sa","gb-sct",
                "se","ch","sn","rs","sc","sl","zw","sg","sx","sk","si","so","es","sj","lk","sh","kn","lc",
                "pm","vc","za","sd","gs","kr","ss","sr","sz","sy","tj","tw","tz","th","tg","tk","to","tt",
                "td","cz","tn","tr","tm","tc","tv","ug","ua","hu","uy","uz","vu","va","ve","ae","us","gb",
                "vn","gb-wls","wf","by","eh","cf","cy"};
        String[] rightGuesses = {"Afghanistan","Egypt","Aland","Albania","Algeria","American Samoa","US Virgin Islands","Andorra","Angola","Anguilla","Antarctica","Antigua and Barbuda","Equatorial Guinea",
                "Argentina","Armenia","Aruba","Azerbaijan","Ethiopia","Australia","Bahamas","Bahrain","Bangladesh","Barbados","Belgium","Belize","Benin","Bermuda","Bhutan","Bolivia","Bosnia and Herzegovina",
                "Botswana","Bouvet Island","Brazil","British Virgin Islands","British Indian Ocean Territory","Brunei","Bulgaria","Burkina Faso","Burundi","Chile","China","Cook Islands","Costa Rica",
                "Curacao","Denmark","Germany","Dominica","Dominican Republic","Djibouti","Ecuador","El Salvador","Ivory Coast","England","Eritrea","Estonia","Falkland Islands","Faroe Islands",
                "Fiji","Finland","France","French Guiana","French Polynesia","French Southern and Antarctic Territories","Gabon","Gambia","Georgia","Ghana","Gibraltar","Grenada","Greece","Greenland",
                "Guadeloupe","Guam","Guatemala","Guernsey","Guinea","Guinea-Bissau","Guyana","Haiti","Heard and McDonald Islands","Honduras","Hong Kong","India","Indonesia","Isle of Man","Iraq",
                "Iran","Ireland","Iceland","Israel","Italy","Jamaica","Japan","Yemen","Jersey","Jordan","Cayman Islands","Cambodia","Cameroon","Canada","Cape Verde","Caribbean Netherlands","Kazakhstan",
                "Qatar","Kenya","Kyrgyzstan","Kiribati","United States lesser island possessions","Cocos Islands","Colombia","Comoros","Republic of Congo","Democratic Republic of Congo","Kosovo",
                "Croatia","Cuba","Kuwait","Laos","Lesotho","Latvia","Lebanon","Liberia","Libya","Liechtenstein","Lithuania","Luxembourg","Macao","Madagascar","Malawi","Malaysia","Maldives","Mali",
                "Malta","Morocco","Marshall Islands","Martinique","Mauritania","Mauritius","Mayotte","Mexico","Micronesia","Moldova","Monaco","Mongolia","Montenegro","Montserrat","Mozambique","Myanmar",
                "Namibia","Nauru","New Caledonia","New Zealand","Nicaragua","Netherlands","Niger","Nigeria","Niue","Northern Ireland","North Korea","Northern Mariana Islands","North Macedonia",
                "Norfolk Island","Norway","Oman","Austria","East Timor","Pakistan","Palestine","Palau","Panama","Papua New Guinea","Paraguay","Peru","Philippines","Pitcairn Islands","Poland",
                "Portugal","Puerto Rico","Reunion","Rwanda","Romania","Russia","Saint Barthélemy","Saint Martin","Solomon Islands","Zambia","Samoa","San Marino","Sao Tome and Principe","Saudi Arabia",
                "Scotland","Sweden","Switzerland","Senegal","Serbia","Seychelles","Sierra Leone","Zimbabwe","Singapore","Sint Maarten","Slovakia","Slovenia","Somalia","Spain","Spitsbergen and Jan Mayen",
                "Sri Lanka","Saint Helenas","Saint Kitts and Nevis","Saint Lucia","Saint Pierre and Miquelon","Saint Vincent and the Grenadines","South Africa","Sudan","South Georgia and the South Sandwich Islands",
                "South Korea","South Sudan","Suriname","Eswatini","Syria","Tajikistan","Taiwan","Tanzania","Thailand","Togo","Tokelau","Tonga","Trinidad and Tobago","Chad","Czech Republic","Tunisia",
                "Türkiye","Turkmenistan","Turks and Caicos Islands","Tuvalu","Uganda","Ukraine","Hungary","Uruguay","Uzbekistan","Vanuatu,Vatican City","Vatican","Venezuela","United Arab Emirates",
                "United States,USA,United States of America","United Kingdom","Vietnam","Wales","Wallis and Futuna","Belarus","Sahrawi Arab Democratic Republic","Central African Republic",
                "Cyprus"};
        try {
            for (int i = 0; i < flagNames.length; i++) {
                RenderedImage image = ImageIO.read(new URL("https://flagcdn.com/w2560/" + flagNames[i] + ".png"));
                Guess guess = new Guess(UUID.randomUUID().toString(), List.of(rightGuesses[i]));
                guessRepository.save(guess);
                Path path = Paths.get(PictureService.picturePath + "flags");
                ImageIO.write(image,"jpg", new File(path + "/" + guess.getUuid() + ".jpg"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}