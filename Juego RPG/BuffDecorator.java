public class BuffDecorator extends CharacterDecorator {
    private String buffName;
    private int extraSpeed;

    public BuffDecorator(HeroCharacter character, String buffName, int extraSpeed) {
        super(character);
        this.buffName = buffName;
        this.extraSpeed = extraSpeed;
    }

    @Override public String getDescription() { 
        return character.getDescription() + " | Buff: " + buffName; 
    }

    @Override public String getAvatar() {
        return character.getAvatar() + " 🌀";
    }
    
    @Override public int getSpeed() { return character.getSpeed() + extraSpeed; }
}
