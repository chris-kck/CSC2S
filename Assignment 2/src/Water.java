public class Water {

    float wDepth;
    float wSurface;
    float tHeight;
    public Water (float wDepth, float tHeight){
        this.wDepth=wDepth; //initial depth
        this.tHeight=tHeight; //initial Height
        this.wSurface= wDepth + tHeight; //initial wSurface level
        //wS = wD + tH
    }

    public void increaseDepth (){
        this.wSurface++; //increasing depth also increases surface
    }
}
