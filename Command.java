public final class TestCommand implements Listener {
    /**
     * Map pour stocker les joueurs en cooldown
     */
    private final HashMap<Player, Long> cooldown = new HashMap<>();

    /**
     * Le temps du cooldown en MS
     */
    private final Long time = 5000L;

    /**
     * Le message envoyé quand le joueur est en cooldown
     */
    private final String msg = "§cAttendez encore {sec} secondes.";

    /**
     * Commande de test
     *
     * @param context Les arguments de la commande
     */
    @Command(name = "test")
    public void handleCommand(final Context<ConsoleCommandSender> context) {
        final Player player = (Player) context.getSender();
        final long now = System.currentTimeMillis();

        if (this.validCooldown(player, now)) {
            this.cooldown.put(player, now);
            player.sendMessage("Tu as executé la ccommande maintenant tu dois attendre 5 secondes avant de refaire ceci.");
        }
    }

    /**
     * Savoir si le cooldown est valide ou pas
     * Note: Si il est en cooldown cela lui envoie le message de cooldown
     *
     * @param player Le cooldown de ce player
     * @param time   Le temps en MS
     */
    public boolean validCooldown(final Player player, final long time) {
        final Long lastCooldown = this.cooldown.get(player);
        if (lastCooldown == null || time - lastCooldown >= this.time)
            return true;

        player.sendMessage(this.msg.replace("{sec}", String.format("%.1f", this.remainingCooldown(player, time))));
        return false;
    }

    /**
     * Connaître le temps restant du cooldown
     *
     * @param player Le cooldown de ce player
     * @param time   Le temps en MS
     */
    public double remainingCooldown(final Player player, final long time) {
        return (this.time - (time - this.cooldown.get(player))) / 1000.0;
    }

    /**
     * Supprimer le joueur de la liste pour éviter le memory leak
     */
    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        this.cooldown.remove(event.getPlayer());
    }
}
