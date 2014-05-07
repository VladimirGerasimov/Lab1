package task1;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

public class DialogWind extends JDialog {

	private final JPanel contentPanel = new JPanel();

	public static void main(String head, String message, String message1) {
		try {
			DialogWind dialog = new DialogWind(head, message, message1);
			dialog.setTitle(head);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DialogWind(String head, String message, String message1) {
		setBounds(100, 100, 300, 140);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(2, 1, 0, 0));
		{
			JLabel lblNewLabel = new JLabel(message);
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblSdjlncsd = new JLabel(message1);
			lblSdjlncsd.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblSdjlncsd);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			buttonPane.add(okButton);
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
		}
	}

}
