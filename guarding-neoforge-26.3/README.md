# Guarding — NeoForge 26.3 portu

Team Abode'nin **Guarding** (Fabric, 1.21.1) modunun Minecraft **26.3 / NeoForge 26.3.0.6-beta** portu.
Orijinal mod CC0 lisanslıdır (bkz. `LICENSE`). Bu port **derlenmemiş, test edilmemiş** bir ilk taslaktır.

## Derleme

Gereksinimler: **JDK 25** ve **Gradle 9.1+** (ModDevGradle 2.0.147, 26.3 için gerekli; 2.0.141 çalışmaz).

Bilgisayarında:

    gradle build

Jar `build/libs/` içinde çıkar. (Wrapper dosyaları eklenmedi; `gradle wrapper --gradle-version 9.1.0` ile üretebilirsin.)

GitHub Actions ile (bilgisayara bir şey kurmadan):
1. github.com'da yeni bir depo aç, bu klasörün içeriğini yükle (`.github/workflows/build.yml` dahil).
2. Depoda **Actions** sekmesine gir, "Build" çalışmasını aç.
3. Bittiğinde sayfanın altındaki **Artifacts → guarding-jar** dosyasını indir.

Derleme hata verirse hata metnini olduğu gibi bana yapıştır.

## 1.21.1 Fabric sürümünden değişenler

| Eski | Yeni |
|---|---|
| `sketch` ConfigManager | NeoForge `ModConfigSpec` (`config/guarding-common.toml`, `config/guarding-startup.toml`) |
| `sketch` ShieldEvents.BLOCKED | NeoForge `LivingShieldBlockEvent` |
| `NetheriteShieldItem`, ShieldItem mixin'leri | Kalkan artık veri bileşenleri (`blocks_attacks`, `enchantable`, `equippable`) ile çalışıyor |
| `ShieldDelayMixin` (5 tick gecikme) | `ModifyDefaultComponentsEvent` ile `blocks_attacks` gecikmesi 0 |
| `NetheriteShieldMixin`, `NetheriteShieldSoundMixin` | `blocks_attacks` bileşeni (ses, cooldown) |
| `ProjectileMixin` + `ProjectileAccessor` | NeoForge data attachment (`GuardingAttachments.PARRIER`) |
| `TriggerKilledByParriedArrowCriterionMixin` + özel criterion | `LivingDeathEvent` + `minecraft:impossible` trigger'ı koddan ödüllendirilir |
| Özel `SmithingTransformShieldRecipe` | Vanilla `smithing_transform` + sonuçtan banner bileşenlerini silen patch |
| `SmithingScreenHandlerMixin` | `SmithingMenuMixin` (banner iadesi) |
| Datagen sınıfları | Kaldırıldı, JSON dosyalar doğrudan `src/main/resources/data` içinde |

## Bilinen eksikler / dikkat

- **Netherite Kalkan görünümü:** Şimdilik vanilla kalkan modelini kullanıyor (özel doku/trim render'ı portlanmadı).
  Özel `NetheriteShieldRenderer`/`NetheriteShieldModel` ve trim desteği ikinci aşama.
- `SmithingTemplateItemMixin` (smithing masasında boş slot kalkan ikonu) portlanmadı, sadece görsel.
- Parrier bilgisi (parry edilen okun sahibi) kayıt dosyasına yazılmaz; ok kaydedilip yeniden yüklenirse sıfırlanır.
- `minecraft:shield` kalkanının büyüleme gücü ve bekleme süresi değişimi `ModifyDefaultComponentsEvent` ile yapılıyor.
- Ayar dosyası adları değişti; eski `guarding.json` ayarları taşınmaz.

## Derleme geçmişi

- Tur 1: 31 hata (26.3'te taşınan sınıflar). API raporuna göre düzeltildi: `advancements.triggers`, `ContextKeySet`, `needsSync/syncVelocity`, 3 parametreli `ModifyDefaultComponentsEvent` lambda'sı, `ConditionalEffect` yeni yapısı.
- Geri tepme (knockback) artık vanilla formülüyle elle uygulanıyor (26.3'te `knockback` imzası değişti).

## Olası derleme sorunları (tahminim)

26.3 API'sini derleyerek doğrulayamadım. En olası hata noktaları: `BlocksAttacks` yapıcı parametreleri,
`Equippable.Builder` metot adları, `LivingShieldBlockEvent` metotları, `SimpleAnimatedParticle`/`ParticleProvider` imzası,
`SimpleCriterionTrigger` ve `CriterionValidator` paket adları, `Identifier` içe aktarmaları.
