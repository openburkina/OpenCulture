package com.openculture.org.service;

import com.openculture.org.config.Constants;
import com.openculture.org.domain.Artiste;
import com.openculture.org.domain.ArtisteOeuvre;
import com.openculture.org.domain.Oeuvre;
import com.openculture.org.domain.enumeration.TypeFichier;
import com.openculture.org.repository.ArtisteOeuvreRepository;
import com.openculture.org.repository.OeuvreRepository;
import com.openculture.org.service.dto.ArtisteDTO;
import com.openculture.org.service.dto.ArtisteOeuvreDTO;
import com.openculture.org.service.dto.OeuvreDTO;
import com.openculture.org.service.dto.TypeOeuvreDTO;
import com.openculture.org.service.mapper.ArtisteMapper;
import com.openculture.org.service.mapper.OeuvreMapper;
import com.openculture.org.web.rest.OeuvreResource;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;


/**
 * Service Implementation for managing {@link Oeuvre}.
 */
@Service
@Transactional
public class OeuvreService {

    private final Logger log = LoggerFactory.getLogger(OeuvreService.class);

    private final OeuvreRepository oeuvreRepository;

    private final OeuvreMapper oeuvreMapper;

    private final ArtisteMapper artisteMapper;

    private final ArtisteOeuvreRepository artisteOeuvreRepository;

    private final InformationCivilService informationCivilService;

    private final RegroupementService regroupementService;

    private final TypeOeuvreService typeOeuvreService;

    private final ArtisteService artisteService;

    private final ArtisteOeuvreService artisteOeuvreService;

    private final UserService userService;

    public OeuvreService(
        OeuvreMapper oeuvreMapper,
        InformationCivilService informationCivilService,
        TypeOeuvreService typeOeuvreService,
        RegroupementService regroupementService,
        OeuvreRepository oeuvreRepository,
        ArtisteMapper artisteMapper,
        ArtisteOeuvreRepository artisteOeuvreRepository,
        ArtisteOeuvreService artisteOeuvreService,
        UserService userService,
        ArtisteService artisteService) {
                this.oeuvreRepository = oeuvreRepository;
                this.artisteMapper = artisteMapper;
                this.artisteOeuvreRepository = artisteOeuvreRepository;
                this.informationCivilService = informationCivilService;
                this.artisteOeuvreService = artisteOeuvreService;
                this.oeuvreMapper = oeuvreMapper;
                this.regroupementService = regroupementService;
                this.typeOeuvreService = typeOeuvreService;
                this.userService = userService;
                this.artisteService = artisteService;
    }

    /**
     * Save a oeuvre.
     *
     * @param oeuvreDTO the entity to save.
     * @return the persisted entity.
     * @throws Exception
     */
    public OeuvreDTO save(OeuvreDTO oeuvreDTO) throws Exception {
        log.debug("Request to save Oeuvre : {}", oeuvreDTO);

        List<ArtisteOeuvreDTO> artisteOeuvres = new ArrayList<>();

        if(oeuvreDTO.getId() != null){
            oeuvreDTO.setRegroupementDTO(regroupementService.findOne(oeuvreDTO.getRegroupementId()).get());
            TypeOeuvreDTO typeOeuvreDTO = typeOeuvreService.findOne(oeuvreDTO.getTypeOeuvreId()).get();
            typeOeuvreDTO.setNbOeuvre(typeOeuvreDTO.getNbOeuvre()+1);
            oeuvreDTO.setTypeOeuvreDTO(typeOeuvreService.save(typeOeuvreDTO));
        }

         File media = new File(oeuvreDTO.getPathFile());
         oeuvreDTO.setFileContent(FileUtils.readFileToByteArray(media));
         String s[] = media.getName().split("\\.");
         oeuvreDTO.setFileName(s[0]);
         oeuvreDTO.setFileExtension(s[1]);

        if (validedOeuvre(oeuvreDTO)) {
            if (oeuvreDTO.getArtistes() != null) {
                oeuvreDTO.getArtistes().forEach(
                    artisteDTO -> {
                        ArtisteOeuvreDTO artisteOeuvre = new ArtisteOeuvreDTO();
                        artisteOeuvre.setArtisteId(artisteDTO.getId());
                        artisteOeuvres.add(artisteOeuvre);
                    }
                );
            } else throw new Exception("Le champs auteur est obligatoire");
            log.debug("\n\nRegroupement : {}",oeuvreDTO.getRegroupementDTO());
            log.debug("Type Oeuvre: {}"+"\n\n",oeuvreDTO.getTypeOeuvreDTO());

            Oeuvre oeuvre = oeuvreMapper.toEntity(oeuvreDTO);
            oeuvre = oeuvreRepository.save(oeuvre);

            List<Long> artsId = oeuvreDTO.getArtistes()
                                .stream().map(ArtisteDTO::getId).collect(Collectors.toList());

            List<Long> artOeuvres = artisteOeuvreRepository.findAllByOeuvreId(oeuvre.getId())
                                .stream().map(ArtisteOeuvre::getArtiste)
                                .map(Artiste::getId).collect(Collectors.toList());

            for (Long long1 : artOeuvres) {
                if (!artsId.contains(long1)) {
                    artisteOeuvreRepository.deleteByArtisteId(long1);
                }
            }

            Oeuvre finalOeuvre = oeuvre;
            artisteOeuvres.forEach(
                artisteOeuvreDTO -> {
                    artisteOeuvreDTO.setOeuvreId(finalOeuvre.getId());
                    if (!artOeuvres.contains(artisteOeuvreDTO.getArtisteId())) {
                        artisteOeuvreService.save(artisteOeuvreDTO);
                    }
            });
            return oeuvreMapper.toDto(oeuvre);
        }
        else {
            log.debug("titre: {}",oeuvreDTO.getTitre());
            log.debug("type: {}",oeuvreDTO.getTypeOeuvreId());
            log.debug("reg: {}",oeuvreDTO.getRegroupementId());
            log.debug("date: {}",oeuvreDTO.getDateSortie());

            throw new Exception("Certains paramètres concernant l'oeuvre sont manquants");
        }
    }

    public boolean validedOeuvre(OeuvreDTO oeuvreDTO){
        if (oeuvreDTO.getTitre().length() > 0
            && oeuvreDTO.getTypeOeuvreId() != null
            && oeuvreDTO.getRegroupementId() != null
            && oeuvreDTO.getDateSortie()!=null
            && oeuvreDTO.getArtistes() != null
            && oeuvreDTO.getFileContent() != null
            && oeuvreDTO.getFileName() != null
            && oeuvreDTO.getFileExtension() != null
            )
            return true;
        return false;
    }

    public static byte[] compressData(byte[] contentFile){
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DeflaterOutputStream dStream = new DeflaterOutputStream(out);
            dStream.write(contentFile);
            dStream.flush();
            dStream.close();

            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] deCompressData(byte[] contentFile){
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InflaterOutputStream iStream = new InflaterOutputStream(out);
            iStream.write(contentFile);
            iStream.flush();
            iStream.close();

            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all the oeuvres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */

    @Transactional(readOnly = true)
    public Page<OeuvreDTO> findComplet(Pageable pageable) {
        log.debug("Request to get all Oeuvres");
    //    return oeuvreRepository.findAll(pageable)
        //    .map(oeuvreMapper::toDto);
        return getOeuvreWithArtiste(oeuvreRepository.findAll(pageable).getContent(), pageable);
    }


    @Transactional(readOnly = true)
    public Page<OeuvreDTO> findRecentsPostsOeuvreByUser(String categorie, Pageable pageable) {
        log.debug("Request to get all Oeuvres");
    //    return oeuvreRepository.findAll(pageable)
        //    .map(oeuvreMapper::toDto);
        String user = this.userService.getUserWithAuthorities().get().getLogin();
        return getOeuvreWithArtiste(oeuvreRepository.findTop5ByTypeOeuvreIntituleAndCreatedByOrderByCreatedDateDesc(categorie,user),pageable);
    }

    @Transactional(readOnly = true)
    public Page<OeuvreDTO> findRecentsPostsOeuvre(String categorie, Pageable pageable) {
        log.debug("Request to get all Oeuvres");
    //    return oeuvreRepository.findAll(pageable)
        //    .map(oeuvreMapper::toDto);
        return getOeuvreWithArtiste(oeuvreRepository.findTop5ByTypeOeuvreIntituleOrderByCreatedDateDesc(categorie),pageable);
    }



    @Transactional(readOnly = true)
    public Page<OeuvreDTO> findCompletForAdmin(String categorie,Pageable pageable) {
        log.debug("Request to get all Oeuvres");

        List<OeuvreDTO> oeuvreDTOList = new ArrayList<>();
        List<Oeuvre> oeuvreList = new ArrayList<>();
        if (categorie != null){
           oeuvreList = oeuvreRepository.findAllByTypeOeuvreIntitule(categorie,pageable);
        } else {
           oeuvreList = oeuvreRepository.findAll(pageable).getContent();
        }
        oeuvreDTOList = oeuvreMapper.toDto(oeuvreList);
        oeuvreDTOList.forEach( oeuvreDTO -> {
            oeuvreDTO.setArtistes(artisteOeuvreRepository.findAllByOeuvreId(
                oeuvreDTO.getId()).stream().map(ArtisteOeuvre::getArtiste).map(artisteMapper::toDto)
                .collect(Collectors.toList()));
//            oeuvreDTOList.add(oeuvreDTO);
        });
        oeuvreDTOList.forEach(oeuvreDTO -> {
            List<String> noms = oeuvreDTO.getArtistes().stream().map(ArtisteDTO::getNom).collect(Collectors.toList());
            String artiste = noms.get(0);
            if (noms.size()>1){
                artiste = artiste+" Feat ";
                for (int i = 1; i < noms.size(); i++) {
                    artiste = artiste+noms.get(i)+" ";
                }
            }
            oeuvreDTO.setNomArtiste(artiste);
        });

        return new PageImpl<>(oeuvreDTOList,pageable,oeuvreDTOList.size());
    }


    @Transactional(readOnly = true)
    public Page<OeuvreDTO> findAllByTypeFichier(TypeFichier typeFichier,Pageable pageable) {
        log.debug("Request to get all Oeuvres");

        List<Oeuvre> oeuvres = oeuvreRepository.findAllByTypeFichier(pageable,typeFichier).getContent();

        return getOeuvreWithArtiste(oeuvres, pageable);

    }

    public Page<OeuvreDTO> getOeuvreWithArtiste(List<Oeuvre> oeuvres, Pageable pageable){

        List<OeuvreDTO> oeuvresDTO = new ArrayList<>();
        for (Oeuvre oeuvre: oeuvres){
            List<String> noms = oeuvre.getArtisteOeuvres().stream().map(ArtisteOeuvre::getArtiste).map(Artiste::getNom).collect(Collectors.toList());
            String artiste = noms.get(0);
            if (noms.size()>1){
                artiste = artiste+" Feat ";
                for (int i = 1; i < noms.size(); i++) {
                    artiste = artiste+noms.get(i)+",";
                }
            }
            OeuvreDTO oeuvreDTO = oeuvreMapper.toDto(oeuvre);
            oeuvreDTO.setNomArtiste(artiste);
            oeuvresDTO.add(oeuvreDTO);
        }
        oeuvreMapper.toDto(oeuvres);
        return new PageImpl<>(oeuvresDTO,pageable,oeuvresDTO.size());

    }

    /**
     * Get one oeuvre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public OeuvreDTO findOne(Long id) {
        log.debug("Request to get Oeuvre : {}", id);
        Oeuvre oeuvre = oeuvreRepository.findById(id).get();
        log.debug("\n\n\n\n\nRequest to get Oeuvre cool : {}", id);
        return oeuvreMapper.toDto(oeuvre);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Object> readMedia(Long id) {
        log.debug("Request to get Oeuvre : {}", id);
        OeuvreDTO oeuvre =  (findOne(id));

        if(isVideo(oeuvre.getFileExtension()))
            return ResponseEntity.ok()
            .contentLength(oeuvre.getFileContent().length)
            .contentType(MediaType.parseMediaType(getContentType(oeuvre.getFileExtension())))
            .body(new InputStreamResource(new ByteArrayInputStream(oeuvre.getFileContent())));
        else {
            return ResponseEntity.ok()
            .contentLength(oeuvre.getFileContent().length)
            .contentType(MediaType.parseMediaType(getContentType(oeuvre.getFileExtension())))
            .body(new InputStreamResource(new ByteArrayInputStream(oeuvre.getFileContent())));
        }
    }

    public String getContentType(String contentType){

        if(contentType.equals("mp4"))
            contentType = Constants.CONTENT_TYPE_MP4;
        else if(contentType.equals("flv"))
            contentType = Constants.CONTENT_TYPE_FLV;
        else if(contentType.equals("wmv"))
            contentType = Constants.CONTENT_TYPE_WMV;
        else if(contentType.equals("avi"))
            contentType = Constants.CONTENT_TYPE_AVI;
        else if(contentType.equals("mp3"))
            contentType = Constants.CONTENT_TYPE_MP3;
        else if(contentType.equals("mp2"))
            contentType = Constants.CONTENT_TYPE_MP2;

        return contentType;
    }

    public boolean isVideo(String contentType){
        if(contentType.equals("flv") || contentType.equals("wmv") || contentType.equals("avi")){
            return true;
        } else {
            return false;
        }
    }


    public void readAudio(){
        // byte[] b;
        // Files.write(Paths.get("audio"),b);
        // File file = FileUtils.writeByteArrayToFile(file, b);
        // InputStream inputStream = new ByteArrayInputStream(b);
        // AudioFormat audioFormat = new AudioFormat();
        // AudioInputStream audioInputStream = new AudioInputStream(inputStream, , length);
    }

    /**
     * Delete the oeuvre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Oeuvre : {}", id);
        List<ArtisteOeuvre> artOeuvres = artisteOeuvreRepository.findAllByOeuvreId(id);
        artisteOeuvreRepository.deleteInBatch(artOeuvres);

        TypeOeuvreDTO typeOeuvreDTO = findOne(id).getTypeOeuvreDTO();
        typeOeuvreDTO.setNbOeuvre(typeOeuvreDTO.getNbOeuvre()-1);
        typeOeuvreService.save(typeOeuvreDTO);

        oeuvreRepository.deleteById(id);
    }

    // public ResponseEntity<StreamingResponseBody> readVideo (String videoName){
    //     try {
    //         StreamingResponseBody streamingResponseBody;
    //         final HttpHeaders httpHeaders = new HttpHeaders();

    //         final Path filePath = getVideoPath(videoName);
    //         final long fileSize = Files.size(filePath);

    //         byte[] buffer = new byte[1024];

    //         httpHeaders.add(Constants.HEADER_CONTENT_TYPE, getContentType());
    //         httpHeaders.add(C, Long.toString(fileSize));
    //         streamingResponseBody = os -> {
    //             try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r")) {
    //                 long pos = 0;
    //                 file.seek(pos);

    //                 while (pos < fileSize) {
    //                     file.read(buffer);
    //                     os.write(buffer);
    //                     pos += buffer.length;
    //                 }

    //                 os.flush();
    //             } catch (Exception ignored) {
    //                 // Noop
    //             }
    //         };
    //         return new ResponseEntity<>(streamingResponseBody, httpHeaders, HttpStatus.OK);

    //     } catch (FileNotFoundException e) {
    //         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //     } catch (IOException e) {
    //         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    public Path getVideoPath(String videoName) throws FileNotFoundException {
        final URL videoResource = OeuvreResource.class.getClassLoader().getResource(videoName);
        if (videoResource != null){
            try {
                return Paths.get(videoResource.toURI());
            } catch (URISyntaxException e) {
                throw new FileNotFoundException();
            }
        }
        throw new FileNotFoundException();
    }

    public String formatedDateToString(Instant date){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM dd,yyyy");
        String dateInString = dateTimeFormatter.format(date);
        return dateInString;
    }
}
