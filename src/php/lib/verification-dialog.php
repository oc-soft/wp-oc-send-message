<dialog class="<?php echo $classes ?>">
  <div class="header"><span class="title"></span></div>
  <div class="contents"></div>
  <div class="footer">
    <div class="command-bar">
      <div class="buttons">
        <button type="button"
          class="ok"
          ><?php echo __('Submit', $i18n_domain); ?></button>
        <button type="button"
          class="cancel"
          ><?php echo __('Cancel', $i18n_domain); ?></button>
      </div>
    </div>
  </div>
</dialog>
<?php
// vi: se ts=2 sw=2 et:
