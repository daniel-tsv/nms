import styles from "./ToggleButton.module.css";

interface ToggleButtonProps<T> {
  options: T[];
  selectedOption: T;
  setSelectedOption: (option: T) => void;
  getOptionLabel?: (option: T) => string; // Optional prop to format labels
}

export const ToggleButton = <T extends string>({
  options,
  selectedOption,
  setSelectedOption,
  getOptionLabel = (option) => option, // Default label formatter
}: ToggleButtonProps<T>) => {
  if (options.length < 2) {
    throw new Error("ToggleButton requires at least two options.");
  }

  return (
    <div className={styles["toggle-button"]}>
      <div className={styles["toggle-button__wrapper"]}>
        <button
          className={`${styles["toggle-button__option"]} ${
            selectedOption === options[0]
              ? styles["toggle-button__option--active"]
              : styles["toggle-button__option--unselected"]
          }`}
          onClick={() => setSelectedOption(options[0])}
        >
          {getOptionLabel(options[0])}
        </button>
        <button
          className={`${styles["toggle-button__option"]} ${
            selectedOption === options[1]
              ? styles["toggle-button__option--active"]
              : styles["toggle-button__option--unselected"]
          }`}
          onClick={() => setSelectedOption(options[1])}
        >
          {getOptionLabel(options[1])}
        </button>
      </div>
    </div>
  );
};
