public abstract class CharacterDecorator implements HeroCharacter {
    protected HeroCharacter character;

    public CharacterDecorator(HeroCharacter character) {
        this.character = character;
    }

    @Override public String getName() { return character.getName(); }
    @Override public String getDescription() { return character.getDescription(); }
    @Override public String getAvatar() { return character.getAvatar(); }
    @Override public int getHp() { return character.getHp(); }
    @Override public int getDamage() { return character.getDamage(); }
    @Override public int getDefense() { return character.getDefense(); }
    @Override public int getSpeed() { return character.getSpeed(); }
}
