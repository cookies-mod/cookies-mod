package dev.morazzer.cookiesmod.config.categories.about.credits;

import java.util.List;

public record Credits(List<Person> maintainers, List<Library> libraries, List<Person> discordStaff,
                      Contributors contributors, List<Person> ideas, Descriptions descriptions) {
}
