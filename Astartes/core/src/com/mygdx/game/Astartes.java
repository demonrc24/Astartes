package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

//应用主类
public  class Astartes extends ApplicationAdapter{
	private SpriteBatch batch;

	private Texture tauTexture;
	private Texture astarteTexture;
	private Array<Sprite> tau;
	private Sprite astarte;
	private Random r;
	private int time;
	private int jump;
	private int point;
	private FileHandle pointFile;
	private BitmapFont font;
	private int maxScore;
    private Music BGMusic;
	public final int WORLD_WIDTH=600;
	public final int WORLD_HEIGHT=200;
	

	
	@Override
	
	
	public void create() {
	batch=new SpriteBatch();

	tauTexture=new Texture(Gdx.files.internal("tau.png"));
	astarteTexture=new Texture(Gdx.files.internal("astartes.jpg"));
	tau=new Array<>();
	astarte=new Sprite(astarteTexture);
	
	r=new Random();


	pointFile=new FileHandle("data/pointMax.txt");
	String str=pointFile.readString();
	if(str.equals(""))maxScore=0;
	else maxScore=Integer.valueOf(str);
	
	font=new BitmapFont();
	font.setColor(Color.BLACK);
	font.getData().setScale(3);
	reset();
	 BGMusic = Gdx.audio.newMusic(Gdx.files.internal("Daredevil.mp3"));
	 BGMusic.setLooping(true);
	 BGMusic.play();
	}
	
	@Override
	public void render() {
	Gdx.gl.glClearColor(1,1,1,1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	act(Gdx.graphics.getDeltaTime());
	batch.begin();

	for(Sprite s : tau)
	s.draw(batch);
	
	astarte.draw(batch);
	font.draw(batch,"MAX  "+String.valueOf(maxScore)+"   "+String.valueOf(point),130,150);
	batch.end();
}
	

	private void reset() {

	astarte.setBounds(60,0,45,60);

	maxScore=Integer.valueOf(pointFile.readString());



	point=0;

	jump=0;

	time=0;

	int i;
	for( i=tau.size-1;i>=0;--i)
		tau.removeIndex(i);
	}



	private void act(float delta) {

	time+=delta*150;

	if(astarte.getY()==0&&Gdx.input.justTouched())jump=20;

	astarte.setY(astarte.getY()+jump);

	if(astarte.getY()>0)jump-=delta*80;
	else {
	astarte.setY(0);
	jump=0;
	}
	

	if(time%15==0)point++;


	if(time%50==0&&r.nextInt(2)==0) {
	Sprite taus=new Sprite(tauTexture);

	taus.setBounds(WORLD_WIDTH, 0, 40,r.nextInt(20)+30);

	tau.add(taus);
	}
	
	Sprite taus;

	for(int i=tau.size-1;i>=0;--i) {

	taus=tau.get(i);

	taus.setX(taus.getX()-500*delta);

	if(astarte.getBoundingRectangle().overlaps(taus.getBoundingRectangle())) {

	if(point>maxScore)pointFile.writeString(String.valueOf(point),false);
	reset();
	}
	if(taus.getX()<0-70)
  tau.removeIndex(i);
	}
	
}
	

	@Override
	public void dispose() {
	batch.dispose();
	astarteTexture.dispose();
	tauTexture.dispose();
	}
}
