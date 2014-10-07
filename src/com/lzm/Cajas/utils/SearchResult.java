package com.lzm.Cajas.utils;

/**
 * Created by Svt on 10/6/2014.
 */
public class SearchResult {
    //response [{"NameId":43000053,"ScientificName":"Rosales","ScientificNameWithAuthors":"Rosales Bercht. & J.
    // Presl","Family":"","RankAbbreviation":"ord.","NomenclatureStatusName":"No opinion","Author":"Bercht. & J. Presl",
    // "DisplayReference":"Prir. Rostlin 231","DisplayDate":"1820","TotalRows":1208},{"NameId":27807452,"ScientificName":
    // "Rosa div Simplicifolia","ScientificNameWithAuthors":"Rosa div Simplicifolia Lindl.","Family":"","RankAbbreviation":"div."
    // ,"NomenclatureStatusName":"No opinion","Author":"Lindl.","DisplayReference":"Ros. Monogr. 1","DisplayDate":"1820","TotalRows":1208},{"NameId":27807292,"ScientificName":"Rosa div Feroces","ScientificNameWithAuthors":"Rosa div Feroces Lindl.","Family":"","RankAbbreviation":"div.","NomenclatureStatusName":"No opinion","Author":"Lindl.","DisplayReference":"Ros. Monogr. 3","DisplayDate":"1820","TotalRows":1208},{"NameId":27807246,"ScientificName":"Rosa div Systylae","ScientificNameWithAuthors":"Rosa div Systylae Lindl.","Family":"","RankAbbreviation":"div.","NomenclatureStatusName":"No opinion","Author":"Lindl.","DisplayReference":"Ros. Monogr. 111","DisplayDate":"1820","TotalRows":1208},{"NameId":27807475,"ScientificName":"Rosa div Rubiginosae","ScientificNameWithAuthors":"Rosa div Rubiginosae Lindl.","Family":"","RankAbbreviation":"div.","NomenclatureStatusName":"No opinion","Author":"Lindl.","DisplayReference":"Ros. Monogr. 84","DisplayDate":"1820","TotalRows":1208},{"NameId":27807468,"ScientificName":"Rosa div Bracteatae","ScientificNameWithAuthors":"Rosa div Bracteatae Lindl.","Family":"","RankAbbreviation":"div.","NomenclatureStatusName":"No opinion","Author":"Lindl.","DisplayReference":"Ros. Monogr. 7","DisplayDate":"1820","TotalRows":1208},{"NameId":100352397,"ScientificName":"Rosanae","ScientificNameWithAuthors":"Rosanae Takht.","Family":"","RankAbbreviation":"superord.","NomenclatureStatusName":"No opinion","Author":"Takht.","DisplayReference":"Sist. Filog. Cvetk. Rast. 264","DisplayDate":"1967","TotalRows":1208},{"NameId":27807280,"ScientificName":"Rosa div Centifoliae","ScientificNameWithAuthors":"Rosa div Centifoliae DC. ex Ser.","Family":"","RankAbbreviation":"div.","NomenclatureStatusName":"No opinion","Author":"DC. ex Ser.","DisplayReference":"Mus. Hist. Nat. Helv.# 1: 2","DisplayDate":"1818","TotalRows":1208},{"NameId":27807473,"ScientificName":"Rosa div Caninae","ScientificNameWithAuthors":"Rosa div Caninae Lindl.","Family":"","RankAbbreviation":"div.","NomenclatureStatusName":"No opinion","Author":"Lindl.","DisplayReference":"Ros. Monogr. 97","DisplayDate":"1820","TotalRows":1208},{"NameId":40026946,"ScientificName":"Rosalesia","ScientificNameWithAuthors":"Rosalesia La Llave","Family":"Asteraceae","RankAbbreviation":"gen.","NomenclatureStatusName":"No opinion","Author":"La Llave","DisplayReference":"Nov. Veg. Descr. 1: 9","DisplayDate":"1824","TotalRows":1208}]
    public String nameId;
    public String scientificName;
    public String scientificNameWithAuthors;
    public String family;
    public String rankAbbreviation;
    public String author;
    public String displayReference;
    public String displayDate;

    public SearchResult(String nameId, String scientificName, String scientificNameWithAuthors, String family, String rankAbbreviation, String author, String displayReference, String displayDate) {
        this.nameId = nameId;
        this.scientificName = scientificName;
        this.scientificNameWithAuthors = scientificNameWithAuthors;
        this.family = family;
        this.rankAbbreviation = rankAbbreviation;
        this.author = author;
        this.displayReference = displayReference;
        this.displayDate = displayDate;
    }
}
