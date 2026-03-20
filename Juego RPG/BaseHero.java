public class BaseHero implements HeroCharacter {
    private String name;
    private String avatar;
    private int hp;
    private int damage;
    private int defense;
    private int speed;
    
    public BaseHero(String name, String avatar, int hp, int damage, int defense, int speed) {
        this.name = name;
        this.avatar = avatar;
        this.hp = hp;
        this.damage = damage;
        this.defense = defense;
        this.speed = speed;
    }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public String getDescription() { return "Héroe Base (" + name + ")"; }

    @Override
    public String getAvatar() { return avatar; }
    
    @Override
    public int getHp() { return hp; }
    
    @Override
    public int getDamage() { return damage; }
    
    @Override
    public int getDefense() { return defense; }
    
    @Override
    public int getSpeed() { return speed; }
}
