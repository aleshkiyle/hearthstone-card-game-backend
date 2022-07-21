package ru.tinkoff.cardgame.game.model.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum CardProvider {
    INSTANCE;

    private final ArrayList<Card> cards = new ArrayList<>();

    {
        cards.add(new Card("Ингрид Ильва", 3, "Белая ведьма, способная овладеть магией.", 2, 1, Spell.SPELL1, 1, CardClass.Witch));
        cards.add(new Card("Лаума", 3, "Ведьма, управляет силами природы, ночами душит спящих, насылает кошмары, подменяет детей.", 2, 4, Spell.SPELL4, 2, CardClass.Witch));
        cards.add(new Card("Геката", 3, "Королева ведьм, олицетворяющая исключительно силы мрака.", 3, 5, Spell.SPELL10, 3, CardClass.Witch));
        cards.add(new Card("Эрихто", 3, "Легендарная ведьма, известная своей ужасающей внешностью и нечестивыми поступками.", 5, 6, Spell.SPELL11, 4, CardClass.Witch));
        cards.add(new Card("Медея", 3, "Ведьма, которая может превращаться в птиц и животных, а также в некоторые предметы.", 4, 4, Spell.SPELL17, 5, CardClass.Witch));
        cards.add(new Card("Шенрон", 3, "Огромный золотой дракон с малиновыми глазами и громадными крыльями.", 1, 2, Spell.SPELL15, 1, CardClass.Dragon));
        cards.add(new Card("Смауг", 3, "Золотисто-красный крылатый дракон, использующий огонь в качестве своего основного оружия.", 3, 5, Spell.SPELL18, 2, CardClass.Dragon));
        cards.add(new Card("Фуцанлун", 3, "Дракон-хранитель подземных сокровищ, отличается от других драконов тем, что не летает.", 2, 1, Spell.SPELL19, 3, CardClass.Dragon));
        cards.add(new Card("Ладон", 3, "Массивный огнедышащий дракон с сотней голов.", 6, 5, Spell.SPELL14, 4, CardClass.Dragon));
        cards.add(new Card("Анкалагон", 3, "Величайший и сильнейший из всех драконов, первый из крылатых «огненных змеев».", 7, 5, Spell.SPELL3, 5, CardClass.Dragon));
        cards.add(new Card("Барбатос", 3, "Демон, умеющий находить спрятанные сокровища и предсказывать будущее.", 2, 2, Spell.SPELL13, 1, CardClass.Demon));
        cards.add(new Card("Парки", 3, "Демон судьбы, если кто и видел его, то тот человек сам демон.", 2, 4, Spell.SPELL22, 2, CardClass.Demon));
        cards.add(new Card("Велиал", 3, "Демон лжи, великий обманщик, покровитель азартных игр - костей и карт и владыка садомитов.", 5, 4, Spell.SPELL5, 3, CardClass.Demon));
        cards.add(new Card("Мулцибер", 3, "Древний демон-архитектор с длинными седыми волосами и глазами, меняющими свой цвет при разном освещении.", 6, 4, Spell.SPELL20, 4, CardClass.Demon));
        cards.add(new Card("Люцифер", 3, "Великий правитель и владыка ада.", 4, 6, Spell.SPELL12, 5, CardClass.Demon));
        cards.add(new Card("Эттин", 3, "Сутулый двуглавый великан с грубыми чертами орка.", 3, 2, Spell.SPELL2, 1, CardClass.Giant));
        cards.add(new Card("Калевипоэг", 3, "Богатырь-великан, борющийся с нечистой силой.", 2, 4, Spell.SPELL8, 2, CardClass.Giant));
        cards.add(new Card("Гундыр", 3, "Чудовищный многоголовый великан, имеющий человеческий облик.", 4, 4, Spell.SPELL9, 3, CardClass.Giant));
        cards.add(new Card("Хрунгнир", 3, "Сильнейший великан в каменных доспехах.", 5, 6, Spell.SPELL16, 4, CardClass.Giant));
        cards.add(new Card("Атлант", 3, "Великан, который держит небо.", 5, 8, Spell.SPELL7, 5, CardClass.Giant));
        cards.add(new Card("Механический голем", 3, "Огромная махина из металла и магии, которая медленно, но верно идет к своей цели.", 1, 4, Spell.SPELL21, 1, CardClass.Machine));
        cards.add(new Card("Механический танк", 3, "Большое механизированное устройство, напоминающее робота и управляемое человеком изнутри.", 2, 3, Spell.SPELL2, 2, CardClass.Machine));
        cards.add(new Card("Железнозубый прыгун", 3, "Воплощение саблезубого тигра в металле, которое также боится воды.", 4, 3, Spell.SPELL6, 3, CardClass.Machine));
        cards.add(new Card("Безумный бот", 3, "Огромный робот, имеющий человекоподобный облик и управляемый компьютером.", 3, 5, Spell.SPELL19, 4, CardClass.Machine));
        cards.add(new Card("Кошмарное слияние", 3, "Это существо, управляемое механизмами, одновременно и ведьма, и дракон, и демон, и великан.", 6, 9, Spell.SPELL16, 5, CardClass.Machine));
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public Card getRandomLvlCard(int maxLvl) {
        List<Card> cards = this.cards.stream().filter(c->c.getLvl()<=maxLvl).toList();
        // FIXME: 12.07.2022
        // random
        return cards.get(new Random().nextInt(cards.size())).clone();
    }

}
